package clud.colorbee.cole.pojo.vo;

import lombok.Data;
import me.zhyd.hunter.entity.ImageLink;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Data
public class ArticleDetailVO {

    private String id;
    private Long keyId;
    private String title;
    private String content;
    private String author;
    private Date releaseDate;
    private String source;
    private String description;
    private String keywords;
    private List<String> tags;
    private Set<ImageLink> imageLinks;
}
