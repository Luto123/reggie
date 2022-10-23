package com.example.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author: YangQin
 * @className: ErrorPageConfig
 * @description: ErrorPageConfig  错误页面配置
 * @date: 2022/10/23 12:37
 * @other:
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        //配置404页面
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/backend/page/error/404.html"));
        //配置500页面
        registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/backend/page/error/500.html"));
    }
}
