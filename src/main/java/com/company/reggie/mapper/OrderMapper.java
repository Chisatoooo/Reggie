package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:46
 *
 * 订单Mapper接口
 **/
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
