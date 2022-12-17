package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-11-下午 03:03
 *
 * 购物车Mapper接口
 **/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
