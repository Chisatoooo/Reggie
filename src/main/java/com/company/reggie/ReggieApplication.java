package com.company.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author WYC
 * @Create 2022-11-27-下午 02:26
 **/
@SpringBootApplication
@Slf4j
//在Spring Boot启动类上使用@ServletComponentScan注解后
//使用@WebServlet、@WebFilter、@WebListener标记的Servlet、Filter、Listener
//就可以自动注册到Servlet容器中，无需其他代码
@ServletComponentScan
//service实现类中加上@Transactional，如果该类中某个业务方法在执行时报错会进行回滚
//在Spring Boot启动类上使用@EnableTransactionManagement开启事务支持
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }
}
