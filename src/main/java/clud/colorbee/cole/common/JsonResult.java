package clud.colorbee.cole.common;

import lombok.Data;

/**
 * 通用消息
 *
 * @param <T>
 * @author heh
 */
@Data
public class JsonResult<T> {

    private int code = 200;
    private String msg;
    private T data;

    /**
     * 成功时候的调用
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<T>(data);
    }

    /**
     * 成功时候的调用
     */
    public static <T> JsonResult<T> success(String msg, T data) {
        return new JsonResult<T>(msg,data);
    }

    /**
     * 失败时候的调用
     */
    public static <T> JsonResult<T> error(int code, String msg) {
        return new JsonResult<T>(code,msg);
    }

    private JsonResult(T data) {
        this.data = data;
    }

    private JsonResult(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    private JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
