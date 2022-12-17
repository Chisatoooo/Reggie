package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.ShoppingCart;
import com.company.reggie.mapper.ShoppingCartMapper;
import com.company.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author WYC
 * @Create 2022-12-11-下午 03:05
 *
 * 购物和Service实现
 **/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
