package com.example.base_blog.exception;

import com.example.base_blog.common.ResultCode;
import lombok.Getter;
import org.springframework.security.core.parameters.P;

@Getter
public class BusinessException extends RuntimeException{

    private Integer code;
    private String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String message){
        super(message);
        this.code = ResultCode.FAILED.getCode();
        this.message = message;
    }

    public BusinessException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
