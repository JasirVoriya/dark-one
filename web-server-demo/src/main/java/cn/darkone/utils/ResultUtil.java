package cn.darkone.utils;


import cn.darkone.entity.vo.ResultMessage;

public class ResultUtil {
    /**
     * 请求成功，并返回给前端数据
     *
     * @param data 返回给前端都数据
     * @return 响应实体类
     */
    public static <T> ResultMessage<T> data(T data) {
        ResultMessage<T> result = new ResultMessage<>();
        result.setCode(200);
        result.setSuccess(true);
        result.setResult(data);
        return result;
    }

    /**
     * 请求出错
     *
     * @param message 返回给前端的错误提示信息
     * @return 响应实体类
     */
    public static <T> ResultMessage<T> error(String message) {
        ResultMessage<T> result = new ResultMessage<>();
        result.setSuccess(false);
        result.setMessage(message);
        result.setCode(400);
        return result;
    }

    /**
     * 请求出错
     *
     * @param message 返回给前端的错误提示信息
     * @param data    返回给前端都数据
     * @return 响应实体类
     */
    public static <T> ResultMessage<T> error(String message, T data) {
        ResultMessage<T> result = new ResultMessage<>();
        result.setSuccess(false);
        result.setResult(data);
        result.setMessage(message);
        result.setCode(400);
        return result;
    }

    /**
     * 请求正确，用来做判断请求是否成功，并不会带上数据
     *
     * @return 响应实体类
     */
    public static <T> ResultMessage<T> success() {
        ResultMessage<T> result = new ResultMessage<>();
        result.setSuccess(true);
        result.setCode(200);
        return result;
    }
}
