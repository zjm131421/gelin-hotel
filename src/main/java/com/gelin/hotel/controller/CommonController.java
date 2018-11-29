package com.gelin.hotel.controller;/**
 * Created by vetech on 2018/11/29.
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/29
 */
@Controller
@RequestMapping("/view")
public class CommonController {

    /**
     * request
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 通用跳转
     *
     * @param page 目标页面
     * @return 目标页面
     */
    @RequestMapping("**/{page}")
    public String view(@PathVariable("page") String page) {
        String uri = request.getRequestURI();
        uri = StringUtils.removeStart(uri, "/view/");
        return uri;
    }

}
