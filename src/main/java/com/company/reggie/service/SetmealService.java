package com.company.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.reggie.dto.SetmealDto;
import com.company.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Author WYC
 * @Create 2022-12-02-下午 07:24
 *
 * 套餐Service接口
 **/
public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时需要保存套餐和菜品的关联
    public void saveWithDish(SetmealDto setmealDto);

    //根据id查询套餐信息和对应的套餐菜品关系信息
    public SetmealDto getByIdWithSetmealDish(Long ids);

    //更新套餐信息，同时更新套餐和菜品的关联信息
    public void updateWithSetmealDish(SetmealDto setmealDto);

    //删除套餐，同时删除套餐和菜品的关联数据
    public void removeWithDish(List<Long> ids);
}
