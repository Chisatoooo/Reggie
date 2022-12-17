package com.company.reggie.controller;

import com.company.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:52
 *
 * 订单明细Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/orderDetail")
public class OrderDetailController {
    @Resource
    private OrderDetailService orderDetailService;
}
