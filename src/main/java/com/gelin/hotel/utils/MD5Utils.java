package com.gelin.hotel.utils;/**
 * Created by vetech on 2018/11/28.
 */

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
public class MD5Utils {

    /**
     * 默认是utf-8编码
     *
     * @param source
     * @return
     */
    public static String MD5Encode(String source) {
        return MD5Encode(source, "UTF-8");
    }
    public static String MD5Encode(String source, String encode) {
        if (source == null) {
            return null;
        }
        if (StringUtils.isBlank(encode)) {
            return null;
        }

        try {
            return DigestUtils.md5Hex(source.getBytes(encode));
        } catch (Exception e) {
            return null;
        }
    }

}
