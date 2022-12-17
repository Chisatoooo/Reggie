package com.company.reggie.controller;

import com.company.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Author WYC
 * @Create 2022-12-03-下午 01:39
 *
 * 文件上传和下载
 **/
@RestController
@Slf4j
@RequestMapping(value = "/common")
public class CommonController {
    @Value("${reggie.path}")
    private String path;

    /**
     * 文件上传
     * @param file
     * @return
     */
    //1、MultipartFile 和 CommonsMultipartFile都是用来接收上传的文件流的
    //2、MultipartFile是一个接口，CommonsMultipartFile是MultipartFile接口的实现类
    //3、使用MultipartFile作为形参接收上传文件时，直接用即可。CommonsMultipartFile作为形参接收上传文件时，必需添加@RequestParam注解
    // （否则会报错：exception is java.lang.NoSuchMethodException: org.springframework.web.multipart.commons.CommonsMultipartFile）
    @PostMapping(value = "/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //判断目录是否存在
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取原文件名的后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //String[] splitName = originalFilename.split("\\.");
        //String suffix = splitName[1];

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String FileName = UUID.randomUUID().toString() + suffix; //UUID.jgp

        //获得最终路径
        //String spath = uploadFile + FileName;
        //File fpath = new File(spath);
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(path + FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(FileName);
    }

    /**
     * 文件下载
     * @param response
     * @param name
     */
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, String name){
        File file = new File(path + name);
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(file);
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream servletOutputStream = response.getOutputStream();
            //设置图片类型文件
            response.setContentType("image/jpeg");
            //定义缓冲区
            byte[] bytes = new byte[1024];
            int len;
            //边读边写
            while((len = fileInputStream.read(bytes)) != -1){
                servletOutputStream.write(bytes, 0, len);
                //刷新
                servletOutputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            servletOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
