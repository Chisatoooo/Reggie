package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.User;
import com.company.reggie.mapper.UserMapper;
import com.company.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author WYC
 * @Create 2022-12-10-下午 02:36
 *
 * 用户信息Service实现
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
