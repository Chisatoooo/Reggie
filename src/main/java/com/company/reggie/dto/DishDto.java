package com.company.reggie.dto;

import com.company.reggie.entity.Dish;
import com.company.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author WYC
 * @Create 2022-12-03-下午 04:14
 **/
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
