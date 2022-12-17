package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.common.CustomException;
import com.company.reggie.dto.DishDto;
import com.company.reggie.entity.Dish;
import com.company.reggie.entity.DishFlavor;
import com.company.reggie.mapper.DishMapper;
import com.company.reggie.service.DishFlavorService;
import com.company.reggie.service.DishService;
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
 * 菜品Service实现
 **/
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private DishService dishService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto){
        //保存菜品的基本信息到菜品表Dish
        this.save(dishDto);
        //菜品id
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //forEach遍历赋值
        //flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

        //快速枚举遍历赋值
        //for (DishFlavor dishFlavor : flavors) {
        //    dishFlavor.setDishId(dishId);
        //}

        //java8新特性stream流遍历赋值
        flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            return dishFlavor;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表Dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param ids
     * @return
     */
    public DishDto getByIdWithFlavor(Long ids) {
        //查询菜品基本信息。从dish表查询
        Dish dish = this.getById(ids);
        DishDto dishDto = new DishDto();
        //拷贝dish表的信息到dishDto表
        BeanUtils.copyProperties(dish, dishDto);
        //查询菜品口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品信息，同时更新口味信息
     * @param dishDto
     */
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //菜品id
        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //清理当前菜品对应口味数据，dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        //添加当前提交的口味数据，dish_flavor表的insert操作
        flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            return dishFlavor;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询菜品状态，确定是否可以删除
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ids != null, Dish::getId, ids);
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("删除菜品中有正在售卖的菜品, 无法删除！");
        }
        //如果可以删除，先删除菜品表dish中的数据
        this.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        //删除口味表dish_flavor中的数据
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

//        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
//        dishList.stream().map(dish -> {
//            Integer status = dish.getStatus();
//            if(status == 0){
//                //停售中的菜品可以删除
//                dishService.remove(lambdaQueryWrapper);
//            }else{
//                //起售状态不可删除
//                throw new CustomException("删除菜品中有正在售卖的菜品, 无法全部删除！");
//            }
//            return dish;
//        }).collect(Collectors.toList());
        //删除口味
    }
}
