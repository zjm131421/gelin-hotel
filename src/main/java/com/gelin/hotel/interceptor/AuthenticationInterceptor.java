package com.gelin.hotel.interceptor;/**
 * Created by vetech on 2018/11/27.
 */

import com.gelin.hotel.core.api.RestResponse;
import com.gelin.hotel.helper.ApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //封装访问日志对象
//        AccessLogVO logVO = AccessLogUtil.getAccessLog(request, response);
        //将日志对象放入线程变量
//        accesslogThreadLocal.set(logVO);
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
//        IUserAuthorityService userAuthorityService = null;
        try {
//            userAuthorityService = applicationContext.getBean(IUserAuthorityService.class);
        } catch (Exception e) {
            return true;
        }
//        if (userAuthorityService == null) {
//            return true;
//        }
//        RestResponse<Boolean> rstRsp = userAuthorityService.accessMkAuthority(request.getRemoteUser(), request.getRequestURI());
//        boolean isAccess = rstRsp.getResult();
        boolean isAccess = true;
        logger.info("user:{},uri:{},mk_isAccess:{}", request.getRemoteUser(), request.getRequestURI(), isAccess);
        if (!isAccess) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden! 没有此模块权限");
        }
        return true;
    }
}
