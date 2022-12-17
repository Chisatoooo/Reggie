package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.common.CustomException;
import com.company.reggie.dto.SetmealDto;
import com.company.reggie.entity.Setmeal;
import com.company.reggie.entity.SetmealDish;
import com.company.reggie.mapper.SetmealMapper;
import com.company.reggie.service.SetmealDishService;
import com.company.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author WYC
 * @Create 2022-12-02-下午 07:25
 *
 * 套餐Service实现
 **/
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal表，执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //遍历赋值id
        setmealDishes.stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish表，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐信息和对应的套餐菜品关系信息
     * @param ids
     * @return
     */
    public SetmealDto getByIdWithSetmealDish(Long ids) {
        //查询套餐基本信息。从setmeal表查询
        Setmeal setmeal = this.getById(ids);
        SetmealDto setmealDto = new SetmealDto();
        //拷贝setmeal表的信息到setmealDto表
        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询套餐菜品关系信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    /**
     * 更新套餐信息，同时更新套餐和菜品的关联信息
     * @param setmealDto
     */
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);
        //套餐id
        Long setmealId = setmealDto.getId();
        //菜品名称
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //清理当前套餐对应的菜品，setmeal_dish的delete操作
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(lambdaQueryWrapper);

        //添加当前提交的套餐数据，setmeal_dish的insert操作
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and stats = 1;
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除！");
        }
        //如果可以删除，先删除套餐表setmeal中的数据
        this.removeByIds(ids);
        //delete from setmeal_dish where setmeal_id in (1,2,3);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        //删除关系表setmeal_dish中的数据
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

}
