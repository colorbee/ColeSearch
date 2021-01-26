package clud.colorbee.cole.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;

/**
 *  关键字
 * @author heh
 * @date 2021/1/26
 */
@Data
@Document(indexName = "spider_keyword", type = "info")
public class Keywords {

    @Id
    private String id;
    @Field(type = FieldType.Long)
    private Long keyId;
    @CompletionField
    private Completion keyword;
}
