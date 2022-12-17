package com.company.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-12-11-下午 02:21
 *
 * 地址簿Mapper接口
 **/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
