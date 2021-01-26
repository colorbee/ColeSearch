package clud.colorbee.cole.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 *
 * @author heh
 * @date 2021/1/26
 */
@Data
@Document(indexName = "spider", type = "info")
public class Article {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long keyId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Keyword)
    private String author;
    @Field(type = FieldType.Date)
    private Date releaseDate;
    @Field(type = FieldType.Keyword)
    private String source;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Keyword)
    private String keywords;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private List<String> tags;

    public boolean isNull() {
        return StringUtils.isEmpty(author) ||
                StringUtils.isEmpty(content) ||
                StringUtils.isEmpty(description);
    }

}
