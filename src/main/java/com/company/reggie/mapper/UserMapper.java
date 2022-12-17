package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-10-下午 02:33
 *
 * 用户信息Mapper接口
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
