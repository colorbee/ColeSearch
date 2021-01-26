package clud.colorbee.cole.repository;

import clud.colorbee.cole.pojo.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {
}
