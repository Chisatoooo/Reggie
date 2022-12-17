package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.entity.AddressBook;
import com.company.reggie.mapper.AddressBookMapper;
import com.company.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Author WYC
 * @Create 2022-12-11-下午 02:23
 *
 * 地址簿Service实现
 **/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
