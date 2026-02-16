package com.zhiyou.knowledge.entity;

import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result<T> {
    // 响应码：200成功，500失败，400参数错误，404资源不存在
    private Integer code;
    // 响应信息
    private String msg;
    // 响应数据
    private T data;

    // 1. 无参构造（必须显式写出来，否则Lombok+Spring可能有兼容问题）
    public Result() {}

    // 2. 全参构造（对应你在success/error中使用的三参数形式）
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功（有数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    // 自定义响应
    public static <T> Result<T> result(Integer code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}