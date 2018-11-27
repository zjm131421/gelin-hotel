package com.gelin.hotel.common;/**
 * Created by vetech on 2018/11/27.
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@Configuration
public class WebConstant {

    /**
     * 全局忽略
     */
    private static final String IGNORURISTR = "/static/**,/**/*.ico,/**/*.css,/**/*.js";

    /**
     * 忽略
     */
    @Value("${web.ignore.uri}")
    private String webIgnoreUri;
    /**
     * 登陆成功跳转的地址
     */
    @Value("${web.loginsuccess.uri}")
    private String webLoginSuccessUri;

    /**
     * 获取默认忽略
     *
     * @return 忽略的url
     */
    public String getIgnorUriStr() {
        //默认忽略
        String ignorUriStr = WebConstant.IGNORURISTR;
        if (StringUtils.isNotBlank(webIgnoreUri)) {
            ignorUriStr = ignorUriStr + "," + webIgnoreUri;
        }
        return ignorUriStr;
    }


    public String getWebLoginSuccessUri() {
        return webLoginSuccessUri;
    }


}
