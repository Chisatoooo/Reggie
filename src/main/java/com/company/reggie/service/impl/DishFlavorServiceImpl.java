package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.Dish;
import com.company.reggie.entity.DishFlavor;
import com.company.reggie.mapper.DishFlavorMapper;
import com.company.reggie.service.DishFlavorService;
import com.company.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author WYC
 * @Create 2022-12-03-下午 03:23
 **/
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
