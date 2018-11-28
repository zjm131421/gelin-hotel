package com.gelin.hotel.config;/**
 * Created by vetech on 2018/11/28.
 */

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;
import com.gelin.hotel.common.WebConstant;
import com.gelin.hotel.service.GateUserDetailsService;
import com.gelin.hotel.service.PassworMd5;
import com.gelin.hotel.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 常量
     */
    @Autowired
    private WebConstant webConstant;

    /**
     * 获取用户信息
     */
    @Autowired
    private GateUserDetailsService detailsService;

    /**
     * 设置cookie有效期 1天
     */
    private static final int TOKEN_SECOND = 60 * 60 * 24;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] ignorUri = StringUtils.trimToEmpty(webConstant.getIgnorUriStr()).split(",");

        http.authorizeRequests()
                .antMatchers(ignorUri).permitAll()//访问：/home 无需登录认证权限
                .anyRequest().authenticated() //其他所有资源都需要认证，登陆后访问
                //.antMatchers("/admin/index").hasAuthority("ADMIN") //登陆后之后拥有“ADMIN”权限才可以访问/hello方法，否则系统会出现“403”权限不足的提示
                .and()
                .formLogin()
                .loginPage("/login")//指定登录页是”/login”
                .permitAll()
                .successHandler(new RestAuthenticationSuccessHandler()) //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
                //.failureHandler(new RestAuthenticationFailureHandler())
                .and()
                .logout().logoutSuccessHandler(new RestLogoutSuccessHandler())
                .permitAll()
                //.invalidateHttpSession(true)
                .and()
                .rememberMe()//登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
                .tokenValiditySeconds(TOKEN_SECOND);

        http.csrf().disable();// 由于使用的是JWT，我们这里不需要csrf
        http.headers().frameOptions().disable();
        http.httpBasic();
        // 添加JWT filter
        // http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
        http.headers().contentTypeOptions().disable();
    }

    /**
     * 登陆成功后的处理
     */
    public class RestAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
                throws ServletException, IOException {
            //登录成功记录登录日志
//            LoginLogUtil.loginLog(request, response, authentication);
            String ip = WebUtils.getRemoteIp(request);
            logger.info(ip + "登陆成功");
            setDefaultTargetUrl(webConstant.getWebLoginSuccessUri());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            //没有配置域名的时候使用从请求中来的域名。
            SSOConfig config = SSOConfig.getInstance();
            //每次都设置 支持多域名
            //            if (StringUtils.isBlank(config.getCookieDomain())) {
            //            }
            String serverName = request.getServerName();
            if (StringUtils.equalsIgnoreCase(serverName, "127.0.0.1") || StringUtils.equalsIgnoreCase(serverName, "localhost")) {
                logger.error("请使用本机的IP（如：192.168.3.97）地址访问系统，不再支持127.0.0.1 localhost方式访问");
            }
            config.setCookieDomain(serverName);

            SSOHelper.setCookie(request, response, SSOToken.create().setIp(request).setId(userDetails.getUsername()).setIssuer(userDetails.getUsername()), false);

            //保存session
//            iUserService.loginSuccess(userDetails.getUsername(), ip, "");

            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    /**
     * 自定义 DaoAuthenticationProvider
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(new PassworMd5());
        return provider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用简单的MD5
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * 登出成功后的处理
     */
    public static class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request,
                                    HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {
            //登出成功记录登出日志
//            LoginLogUtil.logoutLog(request, response, authentication);
            deleteAccessAuthority(request, response, authentication);
            SSOHelper.clearLogin(request, response);
            setDefaultTargetUrl("/login");
            super.onLogoutSuccess(request, response, authentication);
        }

        /**
         * 删除缓存里面的用户权限
         *
         * @param request        request
         * @param response       response
         * @param authentication 权限对象
         */
        private void deleteAccessAuthority(HttpServletRequest request,
                                           HttpServletResponse response, Authentication authentication) {
            try {
                //记录访问日志
//                AccessLogVO logVO = AccessLogUtil.getAccessLog(request, response);
//                ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
//                IAccessLogService accessLogService = applicationContext.getBean(IAccessLogService.class);
//                AccessLogUtil.accesslog(request, response, logVO, false, accessLogService);
            } catch (Exception exception) {
            }
        }
    }
}
