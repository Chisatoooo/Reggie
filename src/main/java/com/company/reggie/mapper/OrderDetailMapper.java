package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:50
 *
 * 订单明细Mapper接口
 **/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
