package clud.colorbee.cole.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 *  分页对象
 * @author heh
 * @date 2021/1/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    private int pageIndex;
    private int pageSize;
    private int count;
    private int totalPage;
    private List<T> data;
}
