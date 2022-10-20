package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author: YangQin
 * @className: GlobalExceptionHandler
 * @description: GlobalExceptionHandler  全局异常处理器
 * @date: 2022/10/20 14:09
 * @other:
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    //处理SQL异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            //证明是违反了唯一约束
            String[] s = ex.getMessage().split(" ");
            String msg = "用户名："+s[2]+"已经存在，换一个吧";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
