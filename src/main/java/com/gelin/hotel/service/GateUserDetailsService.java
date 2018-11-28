package com.gelin.hotel.service;

import com.gelin.hotel.entity.HotelUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 〈获取用户登录信息〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
@Service
public class GateUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户名为空");
        }
        Optional<HotelUser> userOptional = userService.getByUsername(username);
        HotelUser hotelUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误！"));
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        authorities.add(authority);
        return new User(username, hotelUser.getPassword(),
                true, // 是否可用
                true, // 是否过期
                true, // 证书不过期为true
                true, // 账户未锁定为true
                authorities);
    }
}
