package com.it.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class R<T> {
    @Getter
    @Setter
    private Integer code; //编码：1成功，0和其它数字为失败
    @Getter
    @Setter
    private String msg; //错误信息
    @Getter
    @Setter
    private T data; //数据
    @Getter
    @Setter
    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        r.msg="success";
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.data=msg;
        r.msg = "error";
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
