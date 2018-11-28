package com.gelin.hotel.controller;

import com.gelin.hotel.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
@Controller
public class SecurityController {

    /**
     * 日志工具
     */
    private Logger logger = LoggerFactory.getLogger(SecurityController.class);

    /**
     * request
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 注入response
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * 定制登录页面
     */
    @Value("${login.page:login}")
    private String loginPage;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        String serverName = request.getServerName();
        String ip = WebUtils.getRemoteIp(request);
        logger.info("打开登录页面时是获取的访问地址是{},访问来源是{}", serverName, ip);
        model.addAttribute("ServerName", serverName);
        model.addAttribute("RemoteAddr", ip);
        return loginPage;
    }

}
