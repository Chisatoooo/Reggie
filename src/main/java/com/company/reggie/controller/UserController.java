package com.company.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.reggie.common.R;
import com.company.reggie.entity.User;
import com.company.reggie.service.UserService;
import com.company.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author WYC
 * @Create 2022-12-10-下午 02:37
 *
 * 用户信息Controller
 **/
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @param httpSession
     * @return
     */
    @PostMapping(value = "/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成6位随机验证码
            //String code = ValidateCodeUtils.generateValidateCode(6).toString();
            String code = ValidateCodeUtils.randomCode(6);
            log.info("code: {}", code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("锦木千束","", phone, code);

            //需要将生成的验证码保存到Session
            httpSession.setAttribute("code", code);

            return R.success("手机验证码发送成功！");
        }
        return R.error("手机验证码发送失败！");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param httpSession
     * @return
     */
    @PostMapping(value = "/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession){
        log.info("map: {}", map);
        //获取手机号
        String phone = map.get("phone").toString();
        log.info("phone: {}", phone);
        //获取验证码
        String code = map.get("code").toString();
        log.info("code: {}", code);
        //从Session中获取验证码
        Object codeInSession = httpSession.getAttribute("code");
        log.info("codeInSession: {}", codeInSession);
        //进行验证码比对
        if(codeInSession != null && codeInSession.equals(code)){
            //比对成功说明登录成功
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if(user == null){
                //该用户是新用户，自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败！");
    }
}
