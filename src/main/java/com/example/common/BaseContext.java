package com.example.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: YangQin
 * @className: BaseContext
 * @description: BaseContext  封装ThreadLocal工具类，用户保存和获取当前用户id
 * @date: 2022/10/21 9:13
 * @other:
 */
@Slf4j
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        log.info("在线程中设置了的ID："+id);
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        log.info("获取了线程中的id");
        return threadLocal.get();
    }
}
