package com.company.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.reggie.common.BaseContext;
import com.company.reggie.common.CustomException;
import com.company.reggie.entity.*;
import com.company.reggie.mapper.OrderMapper;
import com.company.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author WYC
 * @Create 2022-12-17-下午 02:48
 *
 * 订单Service实现
 **/
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Resource
    private UserService userService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private AddressBookService addressBookService;
    
    @Resource
    private OrderDetailService orderDetailService;
    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lambdaQueryWrapper);
        if(shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，不能下单！");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单！");
        }

        //向订单表插入数据，一条数据
        long orderId = IdWorker.getId(); //订单号
        //进行购物车的金额数据计算，顺便把订单明细给计算出来
        AtomicInteger amount = new AtomicInteger(0);//使用原子类来保存计算的金额结果
        //这个item是集合中的每一个shoppingCartList对象,是在变化的
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) ->{
            //每对item进行一次遍历就产生一个新的orderDetail对象,然后对orderDetail进行设置,然后返回被收集,被封装成一个集合
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());//单份的金额
            //addAndGet进行累加 item.getAmount()单份的金额  multiply乘  item.getNumber()份数
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //向订单表插入数据，一条数据
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        //Amount是指订单总的金额
        orders.setAmount(new BigDecimal(amount.get())); //总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        if (user.getName() != null){
            orders.setUserName(user.getName());
        }
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车
        shoppingCartService.remove(lambdaQueryWrapper);
    }
}
