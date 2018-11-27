package com.gelin.hotel.interceptor;/**
 * Created by vetech on 2018/11/27.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 自定义一个权限拦截器, 继承HandlerInterceptorAdapter类
 * 此处是拦截请求的html页面,模块菜单都是指向了html页面
 *
 * @author zoujiming
 * @since 2018/11/27
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

}
