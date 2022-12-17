package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.reggie.common.R;
import com.company.reggie.dto.SetmealDto;
import com.company.reggie.entity.Category;
import com.company.reggie.entity.Setmeal;
import com.company.reggie.service.CategoryService;
import com.company.reggie.service.SetmealDishService;
import com.company.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author WYC
 * @Create 2022-12-09-下午 03:28
 *
 * 套餐管理Controller
 **/
@RestController
@Slf4j
@RequestMapping(value = "/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息: {}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping(value = "/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行模糊查询
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行分页查询
        setmealService.page(pageInfo, lambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        //设置SetmealDto的records集合
        List<SetmealDto> collect = null;
        collect = records.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(setmeal, setmealDto);
            //分类id
            Long categoryId = setmeal.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(collect);

        return R.success(setmealDtoPage);
    }

    /**
     * 根据id查询套餐信息和对应的套餐菜品关系信息
     * @param ids
     * @return
     */
    @GetMapping(value = "/{ids}")
    public R<SetmealDto> getByIdWithSetmealDish(@PathVariable Long ids){
        SetmealDto byIdWithSetmealDish = setmealService.getByIdWithSetmealDish(ids);
        return R.success(byIdWithSetmealDish);
    }

    /**
     * 更新套餐信息，同时更新套餐和菜品的关联信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto: {}", setmealDto.toString());
        setmealService.updateWithSetmealDish(setmealDto);
        return R.success("修改套餐成功！");
    }

    /**
     * 修改套餐状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping(value = "/status/{status}")
    public R<String> updateSetmealStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        log.info("status: {}", status);
        log.info("ids: {}", ids);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> listSetmeal = setmealService.list(lambdaQueryWrapper);
        listSetmeal.forEach(setmeal -> {
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        });
        return R.success("修改套餐状态成功！");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    //在后端的同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，
    //@RequestBody最多只能有一个，而RequestParam()可以有多个。
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功！");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping(value = "/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
