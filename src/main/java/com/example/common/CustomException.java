package com.example.common;

/**
 * @author: YangQin
 * @className: CustomException
 * @description: CustomException  自定义异常处理
 * @date: 2022/10/21 10:32
 * @other:
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
