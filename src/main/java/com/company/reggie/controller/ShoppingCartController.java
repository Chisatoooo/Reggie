package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.reggie.common.BaseContext;
import com.company.reggie.common.R;
import com.company.reggie.entity.ShoppingCart;
import com.company.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author WYC
 * @Create 2022-12-11-下午 03:06
 *
 * 购物车Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping(value = "/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据: {}", shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        Long dishId = shoppingCart.getDishId();
        if(dishId != null){
            //添加到购物车的是菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{
            //添加到购物车的是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //SQL: select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(lambdaQueryWrapper);
        if(shoppingCartOne != null){
            //如果已经存在，数量+1
            Integer number = shoppingCartOne.getNumber();
            shoppingCartOne.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCartOne);
        }else{
            //如果不存在，添加到购物车，数量默认1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            //把新增的购物车赋给shoppingCartOne
            shoppingCartOne = shoppingCart;
        }
        return R.success(shoppingCartOne);
    }

    /**
     * 查看购物车
     * @param shoppingCart
     * @return
     */
    @GetMapping(value = "/list")
    //GET请求中，因为没有HttpEntity，所以@RequestBody并不适用
    public R<List<ShoppingCart>> list(ShoppingCart shoppingCart){
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping(value = "/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("清空购物车成功！");
    }

    /**
     * 购物车中减少个数
     * @param shoppingCart
     * @return
     */
    @PostMapping(value = "/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();
        if(dishId != null){
            //要减少的是菜品个数
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart dishOne = shoppingCartService.getOne(lambdaQueryWrapper);
            if(dishOne.getNumber() > 1){
                //数量大于1，减少数量
                dishOne.setNumber(dishOne.getNumber() - 1);
                shoppingCartService.updateById(dishOne);
            }else if(dishOne.getNumber() == 1){
                //数量等于1，删除菜品
                shoppingCartService.removeById(dishOne.getId());
            }else{
                return R.error("操作异常！");
            }
            return R.success(dishOne);
        }else{
            //要减少的是套餐个数
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart setmealOne = shoppingCartService.getOne(lambdaQueryWrapper);
            if(setmealOne.getNumber() > 1){
                //数量大于1，减少数量
                setmealOne.setNumber(setmealOne.getNumber() - 1);
                shoppingCartService.updateById(setmealOne);
            }else if(setmealOne.getNumber() == 1){
                //数量等于1，删除套餐
                shoppingCartService.removeById(setmealOne.getId());
            }else{
                return R.error("操作异常！");
            }
            return R.success(setmealOne);
        }
    }
}
