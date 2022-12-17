package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.reggie.common.R;
import com.company.reggie.entity.Category;
import com.company.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author WYC
 * @Create 2022-11-30-下午 07:53
 *
 * 分类Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        //request.setCharacterEncoding("utf-8");
        //response.setContentType("text/html;charset=utf-8");
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分类成功！");
    }

    /**
     * 分类分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/page")
    public R<Page> page(int page, int pageSize){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //执行分页查询
        categoryService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id = {}", ids);
        //categoryService.removeById(ids);

        //根据id删除分类，删除之前需要进行判断
        categoryService.remove(ids);
        return R.success("分类信息删除成功！");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息: {}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功！");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping(value = "/list")
    //GET请求中，因为没有HttpEntity，所以@RequestBody并不适用
    public R<List<Category>> list(Category category){
        //添加条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
