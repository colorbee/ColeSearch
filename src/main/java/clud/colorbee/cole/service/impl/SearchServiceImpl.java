package clud.colorbee.cole.service.impl;

import clud.colorbee.cole.repository.ArticleRepository;
import clud.colorbee.cole.repository.KeywordsRepository;
import clud.colorbee.cole.util.SnowFlake;
import com.google.common.collect.Lists;
import clud.colorbee.cole.common.PageResult;
import clud.colorbee.cole.pojo.Article;
import clud.colorbee.cole.pojo.Keywords;
import clud.colorbee.cole.pojo.vo.ArticleDetailVO;
import clud.colorbee.cole.pojo.vo.ArticleSimpleVO;
import clud.colorbee.cole.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Service
@Slf4j
public class SearchServiceImpl implements ISearchService {

    private final ArticleRepository articleRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final KeywordsRepository keywordsRepository;
    private ExecutorService executorService = Executors.newFixedThreadPool(6);

    public SearchServiceImpl(ArticleRepository articleRepository, ElasticsearchTemplate elasticsearchTemplate, KeywordsRepository keywordsRepository) {
        this.articleRepository = articleRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.keywordsRepository = keywordsRepository;
    }

    @Override
    public boolean addArticle(Article article) {
        article.setId(article.getSource());
        article.setKeyId(SnowFlake.nextId());
        articleRepository.save(article);
        return true;
    }

    @Override
    public ArticleDetailVO getById(String id) {
        Article article = articleRepository.findById(id).get();
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, articleDetailVO);

//        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
//        //模板所在目录，相对于当前classloader的classpath。
//        resolver.setPrefix("templates/");
//        //模板文件后缀
//        resolver.setSuffix(".html");
//        TemplateEngine templateEngine = new TemplateEngine();
//        templateEngine.setTemplateResolver(resolver);
//
//        //构造上下文(Model)
//        Context context = new Context();
//        context.setVariable("title", article.getTitle());
//        context.setVariable("content", article.getContent());
//
//        //渲染模板
//        FileWriter write = null;
//        try {
//            write = new FileWriter("result.html");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        templateEngine.process("staticPage", context, write);
        return articleDetailVO;
    }

    @Override
    public PageResult<ArticleSimpleVO> keySearch(Integer pageIndex, Integer pageSize, String keyword) {
        String preTag = "<font color='#dd4b39'>";
        String postTag = "</font>";
        keyword = keyword.toLowerCase();
        String finalKeyword = keyword;
        executorService.submit(() -> {
            Keywords keywords = keywordsRepository.findById(finalKeyword).orElse(new Keywords());
            if (StringUtils.isBlank(keywords.getId())) {
                Completion completion = new Completion(new String[]{finalKeyword});
                completion.setWeight(1);
                keywords.setKeyword(completion);
                keywords.setId(finalKeyword);
                keywords.setKeyId(SnowFlake.nextId());
            } else {
                keywords.getKeyword().setWeight(keywords.getKeyword().getWeight() + 1);
            }
            Keywords save = keywordsRepository.save(keywords);
            log.info("save：{}", save);
        });
        Pageable pageInfo = PageRequest.of(pageIndex - 1, pageSize);
        //查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("title", keyword).boost(20.0f));
        boolQueryBuilder.should(QueryBuilders.matchQuery("description", keyword).boost(2.0f));
        boolQueryBuilder.should(QueryBuilders.matchQuery("content", keyword));
        //排序
        SortBuilder sortBuilder = SortBuilders.scoreSort();
        //build
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        SearchQuery searchQuery = queryBuilder
                .withPageable(pageInfo)
                .withQuery(boolQueryBuilder)
                .withSort(sortBuilder)
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag))
                .build();
        log.info("DSL：{}", searchQuery.getQuery().toString());
        Page<Article> searchRes = elasticsearchTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits searchHits = response.getHits();
                SearchHit[] hits = searchHits.getHits();
                List<Article> articleList = Lists.newArrayList();
                for (SearchHit hit : hits) {
                    Article article = new Article();
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    article.setId(sourceAsMap.get("id").toString());
                    article.setSource(sourceAsMap.get("source").toString());
                    article.setTags((List<String>) sourceAsMap.get("tags"));
                    article.setKeywords(sourceAsMap.get("keywords") == null ? null : sourceAsMap.get("keywords").toString());
                    article.setContent(sourceAsMap.get("content") == null ? null : sourceAsMap.get("content").toString());
                    article.setAuthor(sourceAsMap.get("author") == null ? null : sourceAsMap.get("author").toString());
                    article.setKeyId(Long.valueOf(sourceAsMap.get("keyId").toString()));
                    HighlightField title = hit.getHighlightFields().get("title");
                    if (title != null) {
                        article.setTitle(title.fragments()[0].toString());
                    } else {
                        article.setTitle(sourceAsMap.get("title") == null ? null : sourceAsMap.get("title").toString());
                    }
                    HighlightField description = hit.getHighlightFields().get("description");
                    if (description != null) {
                        article.setDescription(description.fragments()[0].toString() + "......");
                    } else {
                        article.setDescription(sourceAsMap.get("description") == null ? null : sourceAsMap.get("description").toString() + "......");
                    }
                    articleList.add(article);
                }
                return new AggregatedPageImpl<>((List<T>) articleList,pageable,searchHits.getTotalHits());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> type) {
                return null;
            }
        });
        List<Article> content = searchRes.getContent();
//        log.info("keySearch content : {}", content);

        List<ArticleSimpleVO> articleSimpleVOList = Lists.newArrayList();
        content.forEach(article -> {
            ArticleSimpleVO articleSimpleVO = new ArticleSimpleVO();
            articleSimpleVO.setTitle(article.getTitle());
//            if (Objects.isNull(article.getDescription()) || article.getDescription().length() < 500) {
//                articleSimpleVO.setDescription(article.getDescription());
//            } else {
//                String substring = StringUtils.substring(article.getDescription(), 0, 500);
//                articleSimpleVO.setDescription(substring + "......");
//            }
            articleSimpleVO.setDescription(article.getDescription());
            articleSimpleVO.setId(article.getId());
            articleSimpleVO.setKeyId(article.getKeyId().toString());
            articleSimpleVOList.add(articleSimpleVO);
        });
        PageResult<ArticleSimpleVO> ret = new PageResult<>();
        ret.setData(articleSimpleVOList);
        ret.setPageIndex(searchRes.getNumber() + 1);
        ret.setPageSize(searchRes.getSize());
        ret.setTotalPage(searchRes.getTotalPages());
        ret.setCount(((Long) searchRes.getTotalElements()).intValue());
        return ret;
    }

    @Override
    public List<String> autoComplete(String keyword) {
        Client client = elasticsearchTemplate.getClient();
        //field的名字,前缀(输入的text),以及大小size
        CompletionSuggestionBuilder suggestionBuilderDistrict = SuggestBuilders
                .completionSuggestion("keyword").skipDuplicates(true)
                .prefix(keyword).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        //添加suggest
        suggestBuilder.addSuggestion("suggestDistrict", suggestionBuilderDistrict);

        //设置查询builder的index,type,以及建议
        SearchRequestBuilder requestBuilder = client.prepareSearch("spider_keyword").setTypes("info").suggest(suggestBuilder);

        SearchResponse response = requestBuilder.get();
        //suggest实体
        Suggest suggest = response.getSuggest();

        //set
        List<String> suggestList = Lists.newArrayList();
        if (Objects.nonNull(suggest)) {
            //获取suggest,name任意string
            Suggest.Suggestion result = suggest.getSuggestion("suggestDistrict");
            for (Object term : result.getEntries()) {
                if (term instanceof CompletionSuggestion.Entry) {
                    CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                    if (!item.getOptions().isEmpty()) {
                        //若item的option不为空,循环遍历
                        for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                            String tip = option.getText().toString();
                            if (!suggestList.contains(tip)) {
                                suggestList.add(tip);
                            }
                        }
                    }
                }
            }
        }
        return suggestList;
    }

}
