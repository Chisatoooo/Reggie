package com.company.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author WYC
 * @Create 2022-11-30-下午 07:33
 *
 * 菜品Mapper接口
 **/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
