package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.reggie.common.R;
import com.company.reggie.entity.Orders;
import com.company.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:49
 *
 * 订单Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping(value = "/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据，{}", orders);
        orderService.submit(orders);
        return R.success("下单成功！");
    }

    /**
     * 查看订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/userPage")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, lambdaQueryWrapper);

        return R.success(pageInfo);
    }
}
