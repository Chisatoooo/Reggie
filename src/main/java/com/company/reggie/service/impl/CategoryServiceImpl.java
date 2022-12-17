package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.common.CustomException;
import com.company.reggie.entity.Category;
import com.company.reggie.entity.Dish;
import com.company.reggie.entity.Setmeal;
import com.company.reggie.mapper.CategoryMapper;
import com.company.reggie.service.CategoryService;
import com.company.reggie.service.DishService;
import com.company.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author WYC
 * @Create 2022-11-30-下午 07:43
 *
 * 分类Service实现
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        //构造条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(dishCount > 0){
            //已关联菜品，抛出一个业务异常
            throw new CustomException("当前分类已关联菜品，无法删除！");
        }

        //构造条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        if(setmealCount > 0){
            throw new CustomException("当前分类已关联套餐，无法删除！");
        }

        //正藏删除分类
        super.removeById(ids);
    }
}
