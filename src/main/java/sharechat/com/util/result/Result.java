package sharechat.com.util.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -3960261604605958516L;

    private int code;
    private String msg;
    private T data = null;

    public static <T> Result<T> success() {
        return new Result<>();
    }

    /**
     * 成功
     *
     * @param data 自定义返回数据
     * @param <T> 返回类泛型，但不能为String
     * @return 通用返回Result
     * */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    /**
     * 成功
     *
     * @param msg 自定义返回消息
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> success(String msg) {
        return new Result<>(msg);
    }

    /**
     * 成功
     *
     * @param msg 自定义返回消息
     * @param data 自定义返回数据
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(msg, data);
    }

    /**
     * 失败
     *
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> error() {
        return new Result<>(ResultCode.ERROR);
    }

    /**
     * 失败
     *
     * @param msg 自定义返回消息
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> error(String msg) {
        return new Result<>(msg);
    }

    /**
     * 失败
     *
     * @param code 自定义状态码
     * @param msg 自定义返回消息
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * 失败
     *
     * @param resultCode CodeMsg, 参数:
     *                   code 状态码
     *                   msg 返回消息
     * @param <T> 返回类泛型
     * @return 通用返回Result
     * */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode);
    }

    private Result() {
        this(ResultCode.SUCCESS);
    }

    private Result(T data) {
        this(ResultCode.SUCCESS, data);
    }

    private Result(String msg) {
        this(ResultCode.SUCCESS.getCode(), msg);
    }

    private Result(String msg, T data) {
        this(ResultCode.SUCCESS.getCode(), msg, data);
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }

    private Result(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg());
    }

    private Result(ResultCode resultCode, T data) {
        this(resultCode);
        this.data = data;
    }
}
