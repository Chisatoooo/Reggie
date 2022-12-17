package com.company.reggie.common;

/**
 * @Author WYC
 * @Create 2022-12-02-下午 07:52
 *
 * 自定义业务异常类
 **/
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
