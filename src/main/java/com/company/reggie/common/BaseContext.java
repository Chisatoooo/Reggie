package com.company.reggie.common;

/**
 * @Author WYC
 * @Create 2022-11-29-下午 06:35
 *
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户的id
 **/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
