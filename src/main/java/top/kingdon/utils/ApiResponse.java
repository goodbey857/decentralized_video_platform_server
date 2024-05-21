package top.kingdon.utils;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse extends HashMap<String, Object> {

    public ApiResponse() {
        put("code", 0);
        put("msg", "success");
    }

    public static ApiResponse error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ApiResponse error(String msg) {
        return error(500, msg);
    }

    public static ApiResponse error(int code, String msg) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("code", code);
        apiResponse.put("msg", msg);
        return apiResponse;
    }

    public static  ApiResponse ok() {
        return new ApiResponse();
    }

    public static ApiResponse ok(String msg) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("msg", msg);
        return apiResponse;
    }

    public static ApiResponse ok(String key, Object value) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put(key, value);
        return apiResponse;
    }

    public ApiResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public ApiResponse putMap(Map map) {
        this.putAll(map);
        return this;
    }


}
