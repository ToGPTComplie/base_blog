package com.example.base_blog.common;

import lombok.Data;

import static com.example.base_blog.common.ResultCode.SUCCESS;

@Data
public class Result<T> {

    private T data;

    private Integer code;

    private String message;

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(SUCCESS.getCode());
        result.setMessage(SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message){
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
