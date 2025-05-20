package cn.edu.sdu.java.server.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 前端 HTTP 请求返回数据对象
 * Integer code 返回代码 0 正确返回 1 错误返回信息
 * Object data 返回数据对象
 * String msg 返回正确错误信息
 */
@Setter
@Getter
public class DataResponse {
    private Integer code;
    private Object data;
    private String msg;

    public DataResponse() {}

    public DataResponse(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    // 成功返回（带数据和消息）
    public static DataResponse ok(Object data, String msg) {
        return new DataResponse(0, data, msg);
    }

    // 成功返回（无数据）
    public static DataResponse ok() {
        return new DataResponse(0, List.of(), "成功");
    }


    // 错误返回（带错误码和消息）
    public static DataResponse error(Integer code, String msg) {
        return new DataResponse(code, null, msg);
    }

    // 快速构建错误响应
    public static DataResponse getReturnMessageError(String msg) {
        return error(1, msg); // 默认错误码 1 表示业务错误
    }
}
