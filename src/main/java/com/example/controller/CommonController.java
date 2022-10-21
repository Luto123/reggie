package com.example.controller;

import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: YangQin
 * @className: CommonController
 * @description: CommonController  主要用于文件上传和下载
 * @date: 2022/10/21 14:39
 * @other:
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传方法
     *
     * @param file 文件
     * @return
     */
    @PostMapping("/upload")//对参数名有要求
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        // file 为一直临时文件，需要转存到指定位置，否则完成传输后临时文件会删除
        //获取原始图片格式
        String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //生成UUID
        String fileName = UUID.randomUUID() + substring;
        File dir = new File(basePath);
        try {
            if (!dir.exists()) {
                //如果目录不存在，需要创建
                dir.mkdirs();
            }
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name     文件名
     * @param response 响应
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try {
            //输入流，通过输入流读取文件
            FileInputStream is = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将数据写回浏览器
            ServletOutputStream os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
                os.flush();
            }
            is.close();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
