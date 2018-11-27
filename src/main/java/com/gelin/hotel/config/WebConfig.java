package com.gelin.hotel.config;/**
 * Created by vetech on 2018/11/27.
 */

import com.gelin.hotel.common.WebConstant;
import com.gelin.hotel.interceptor.AuthenticationInterceptor;
import com.gelin.hotel.interceptor.SSOSpringInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 常量
     */
    @Autowired
    private WebConstant webConstant;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //如果我们要指定一个绝对路径的文件夹（如 H:/myimgs/ ），则只需要使用 addResourceLocations 指定即可。
        //registry.addResourceHandler("/myimgs/**").addResourceLocations("file:H:/myimgs/");
        //setCacheControl 为了避免客户端重复获取资源
        final int maxAge = 30;
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").setCacheControl(CacheControl.maxAge(maxAge, TimeUnit.MINUTES));
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:" + webConstant.getWebLoginSuccessUri());
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        String[] e = StringUtils.trimToEmpty(webConstant.getIgnorUriStr()).split(",");
        Set<String> pset = new TreeSet<String>();
        pset.add("/login");
        for (String str : e) {
            if (StringUtils.isNotBlank(str)) {
                pset.add(str);
            }
        }
        String[] e2 = pset.toArray(new String[]{});
        // kisso 拦截器配置
//        registry.addInterceptor(new SSOSpringInterceptor()).addPathPatterns("/**").excludePathPatterns(e2);
        //权限拦截器
//        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns("/**");
    }
}
