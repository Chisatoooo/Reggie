package com.company.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.reggie.entity.Category;

/**
 * @Author WYC
 * @Create 2022-11-30-下午 07:40
 *
 * 分类Service接口
 **/
public interface CategoryService extends IService<Category> {
    /**
     * 删除菜品，需要判断
     * @param ids
     */
    public void remove(Long ids);
}
