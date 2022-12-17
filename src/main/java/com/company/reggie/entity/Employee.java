package com.company.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author WYC
 * @Create 2022-11-27-下午 03:06
 *
 * 员工实体类
 **/
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    //Long型数据精度丢失问题解决方法1：@JsonSerialize(using = ToStringSerializer.class)
    //@JsonSerialize(using = ToStringSerializer.class)
    //Long型数据精度丢失问题解决方法2：扩展mvc框架的消息转换器，重写WebMvcConfig的extendMessageConverters方法
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber; //身份证号码

    private Integer status;

    @TableField(fill = FieldFill.INSERT) //插入式填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT) //插入式填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Long updateUser;
}
