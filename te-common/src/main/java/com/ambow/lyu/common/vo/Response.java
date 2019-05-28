package com.ambow.lyu.common.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Response extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Response() {
        put("code", 0);
        put("msg", "success");
    }

    public static Response error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Response error(String msg) {
        return error(500, msg);
    }

    public static Response error(int code, String msg) {
        Response response = new Response();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }

    public static Response ok(String msg) {
        Response response = new Response();
        response.put("msg", msg);
        return response;
    }

    public static Response ok(Map<String, Object> map) {
        Response response = new Response();
        response.putAll(map);
        return response;
    }

    public static Response ok() {
        return new Response();
    }

    @Override
    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
