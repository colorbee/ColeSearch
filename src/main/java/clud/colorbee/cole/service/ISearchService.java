package clud.colorbee.cole.service;

import clud.colorbee.cole.common.PageResult;
import clud.colorbee.cole.pojo.Article;
import clud.colorbee.cole.pojo.vo.ArticleDetailVO;
import clud.colorbee.cole.pojo.vo.ArticleSimpleVO;

import java.util.List;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
public interface ISearchService {

    /**
     * 添加文章
     * @return
     * @param article
     */
    boolean addArticle(Article article);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    ArticleDetailVO getById(String id);

    /**
     * 关键字查询
     *
     * @param pageIndex
     * @param pageSize
     * @param keyword
     * @return
     */
    PageResult<ArticleSimpleVO> keySearch(Integer pageIndex,Integer pageSize,String keyword);

    /**
     * 自动补全
     *
     * @param keyword
     * @return
     */
    List<String> autoComplete(String keyword);
}
