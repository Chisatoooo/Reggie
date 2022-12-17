package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-02-下午 07:20
 *
 * 菜品Mapper接口
 **/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
