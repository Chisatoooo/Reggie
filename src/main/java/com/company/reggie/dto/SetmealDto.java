package com.company.reggie.dto;

import com.company.reggie.entity.Setmeal;
import com.company.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @Author WYC
 * @Create 2022-12-09-下午 03:24
 **/
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

