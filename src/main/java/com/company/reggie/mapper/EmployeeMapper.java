package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-11-27-下午 03:11
 *
 * 员工Mapper接口
 **/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
