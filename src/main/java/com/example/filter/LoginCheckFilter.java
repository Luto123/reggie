package com.example.filter;

import com.alibaba.fastjson.JSON;
import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: YangQin
 * @className: LoginCheckFilter
 * @description: LoginCheckFilter 用户是否完成登录的过滤器
 * @date: 2022/10/20 11:09
 * @other:
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，允许通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String[] excludeUrls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //获取请求的URI
        String uri = request.getRequestURI();
        log.info("拦截到请求：{}", uri);
        //判断请求是否需要处理
        if (check(excludeUrls, uri)) {
            //放行,不需要处理
            log.info("本次请求{}不需要处理", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        Object employee = request.getSession().getAttribute("employee");
        //判断登陆状态
        if (request.getSession().getAttribute("employee") != null) {
            //已经登陆
            log.info("用户已经登陆：ID = {}", request.getSession().getAttribute("employee"));
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录，拦截");
        //如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查当次请求是否需要放行
     *
     * @param uris       不拦截uri数组
     * @param requestUri 当此请求uri
     * @return 是否拦截
     */
    public boolean check(String[] uris, String requestUri) {
        for (String s : uris) {
            if (PATH_MATCHER.match(s, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
