package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.Employee;
import com.company.reggie.mapper.EmployeeMapper;
import com.company.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author WYC
 * @Create 2022-11-27-下午 03:16
 *
 * 员工Service实现
 **/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
