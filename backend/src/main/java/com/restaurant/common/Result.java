package com.restaurant.common;

import lombok.Data;

/**
 * 统一返回结果类
 * @param <T> 泛型，用于封装具体数据
 */
@Data
public class Result<T> {
    
    /**
     * 状态码：200表示成功，其他表示失败
     */
    private Integer code;
    
    /**
     * 提示信息
     */
    private String msg;
    
    /**
     * 具体数据
     */
    private T data;
    
    /**
     * 成功返回
     * @param data 具体数据
     * @param <T> 泛型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回，无数据
     * @param <T> 泛型
     * @return Result对象
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        return result;
    }
    
    /**
     * 失败返回
     * @param code 状态码
     * @param msg 提示信息
     * @param <T> 泛型
     * @return Result对象
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    
    /**
     * 失败返回，默认状态码为500
     * @param msg 提示信息
     * @param <T> 泛型
     * @return Result对象
     */
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }
    
}