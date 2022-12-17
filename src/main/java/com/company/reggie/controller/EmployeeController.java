package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.reggie.common.R;
import com.company.reggie.entity.Employee;
import com.company.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author WYC
 * @Create 2022-11-27-下午 03:15
 *
 * 员工Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     * RequestBody用来接收前端传递给后端的json字符串
     * request用来登录后存储在session中，后续用getSession()方法
     */
    @PostMapping(value = "/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("没有查询到此用户，登录失败！");
        }

        //4、密码比对，如果不一致则返回失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误，登录失败！");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("帐号已禁用！");
        }

        //6、登陆成功，将员工id存入Session并返回成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping(value = "/logout")
    public R<String> logout(HttpServletRequest request){
        //清楚Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息: {}", employee.toString());
        //设置初始密码为123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间
        //employee.setCreateTime(LocalDateTime.now());
        //设置修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //获得当前用户的id
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //设置创建人
        //employee.setCreateUser(empId);
        //设置修改人
        //employee.setUpdateUser(empId);
        //保存用户
        employeeService.save(employee);
        return R.success("新增员工成功！");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping(value = "/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page: {}, pageSize: {}, name: {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行分页查询
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateInformation(HttpServletRequest request, @RequestBody Employee employee){
        log.info("该员工信息: {}", employee.toString());
        //通过Session获取本次登录的员工id
        //Long empId = (Long)request.getSession().getAttribute("employee");
        //更新修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //更新修改id
        //employee.setUpdateUser(empId);
        //执行更新操作
        employeeService.updateById(employee);
        return R.success("修改状态成功！");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public R<Employee> getEmployeeInformationById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        //获取将被修改的员工id
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
