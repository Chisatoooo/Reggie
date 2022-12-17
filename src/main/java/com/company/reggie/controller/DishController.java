package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.reggie.common.R;
import com.company.reggie.dto.DishDto;
import com.company.reggie.entity.Category;
import com.company.reggie.entity.Dish;
import com.company.reggie.entity.DishFlavor;
import com.company.reggie.service.CategoryService;
import com.company.reggie.service.DishFlavorService;
import com.company.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author WYC
 * @Create 2022-12-03-下午 03:27
 *
 * 菜品Controller
 **/
@RestController
@RequestMapping(value = "/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping(value = "/page")
    public R<Page> page(int page, int pageSize,String name){
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行模糊查询
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, lambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //获取Dish的records集合
        List<Dish> records = pageInfo.getRecords();
        //设置DishDto的records集合
        List<DishDto> collect = null;
        collect = records.stream().map(dish -> {
            //初始化dishDto对象
            DishDto dishDto = new DishDto();
            //把dish中的属性拷贝到dishDto中
            BeanUtils.copyProperties(dish, dishDto);
            //获取分类id
            Long categoryId = dish.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //获取分类名称
                String categoryName = category.getName();
                //将名称传入dishDto
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(collect);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param ids
     * @return
     */
    @GetMapping("/{ids}")
    public R<DishDto> getByIdWithFlavor(@PathVariable Long ids){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(ids);
        return R.success(byIdWithFlavor);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功！");
    }

    /**
     * 修改菜品状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping(value = "/status/{status}")
    public R<String> updateDishStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        log.info("status: {}", status);
        log.info("ids: {}", ids);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ids != null, Dish::getId, ids);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
//        for(Dish dish : dishList){
//            if(dish != null){
//                dish.setStatus(status);
//                dishService.updateById(dish);
//            }
//        }
        dishList.stream().map(dish -> {
            if(dish != null){
                dish.setStatus(status);
                dishService.updateById(dish);
            }
            return dish;
        }).collect(Collectors.toList());
        return R.success("修改菜品状态成功！");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids){
        log.info("ids: {}", ids);
        dishService.removeWithDish(ids);
        return R.success("删除菜品成功！");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping(value = "/list")
//    public R<List<Dish>> list(Dish dish){
//        //构造查询条件
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        //添加条件，查询状态为1（起售状态）的菜品
//        lambdaQueryWrapper.eq(Dish::getStatus, 1);
//        //添加排序条件
//        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//        return R.success(list);
//    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping(value = "/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);

        List<DishDto> dishDtoList = list.stream().map(item -> {
            //初始化dishDto对象
            DishDto dishDto = new DishDto();
            //把dish中的属性拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //获取分类名称
                String categoryName = category.getName();
                //将名称传入dishDto
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL: select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}

