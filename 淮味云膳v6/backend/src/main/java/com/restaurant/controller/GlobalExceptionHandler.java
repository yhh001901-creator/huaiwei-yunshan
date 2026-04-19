package com.restaurant.controller;

import com.restaurant.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理404错误
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result<?> handle404(NoHandlerFoundException e) {
        return Result.error(404, "接口不存在");
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
    
}