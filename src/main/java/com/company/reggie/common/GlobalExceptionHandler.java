package com.company.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author WYC
 * @Create 2022-11-28-下午 08:31
 *
 * 全局异常处理
 **/
@Slf4j
@RestControllerAdvice
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());

        //重复异常
        if(exception.getMessage().contains("Duplicate entry")){
            //System.out.println("异常: " + exception.getMessage());
            String[] split = exception.getMessage().split(" ");
            //substring左闭右开
            String name = split[2].substring(1, split[2].length() - 1);
            String msg = name + " 已存在！";
            //String msg = split[2] + " 已存在！";
            //System.out.println("信息: " + msg);
            return R.error(msg);
        }

        //其他未知异常
        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
