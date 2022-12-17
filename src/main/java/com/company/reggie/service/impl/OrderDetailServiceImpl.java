package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.OrderDetail;
import com.company.reggie.mapper.OrderDetailMapper;
import com.company.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:51
 *
 * 订单明细Service实现
 **/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
