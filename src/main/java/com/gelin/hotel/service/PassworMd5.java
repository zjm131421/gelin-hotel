package com.gelin.hotel.service;

import com.gelin.hotel.utils.MD5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
public class PassworMd5 implements PasswordEncoder{
    @Override
    public String encode(CharSequence charSequence) {
        final int md5Len = 32;
        String password = charSequence.toString();
        if (password.length() == md5Len) {
            return password;
        }
        return MD5Utils.MD5Encode(password);
    }

    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        String password = encode(charSequence);
        return password.equalsIgnoreCase(encodedPassword);
    }

    public static void main(String[] args) {

    }
}
