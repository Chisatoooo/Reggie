package com.company.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.reggie.entity.Orders;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:45
 *
 * 订单Service接口
 **/
public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
