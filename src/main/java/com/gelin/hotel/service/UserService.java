package com.gelin.hotel.service;

import com.gelin.hotel.core.BaseServiceImpl;
import com.gelin.hotel.entity.HotelUser;
import com.gelin.hotel.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@Service
public class UserService extends BaseServiceImpl<UserMapper,HotelUser>{

    public HotelUser require(long id){
        return getById(id).orElseThrow(() -> new RuntimeException(String.format("未查询到用户信息：%d",id)));
    }

    public Optional<HotelUser> getById(long id){
        return Optional.ofNullable(selectById(id));
    }

    public Optional<HotelUser> getByUsername(String username){
        return Optional.ofNullable(this.baseMapper.getByUsername(username));
    }

    public HotelUser add(HotelUser user){
        initData(user);
        storeAdd(user);
        return user;
    }

    public HotelUser update(HotelUser user){
        doUpdate(user);
        storeUpdate(user);
        return user;
    }

    public HotelUser enable(HotelUser user, boolean enable){
        user.setEnable(enable);
        storeUpdate(user);
        return user;
    }

    private void doUpdate(HotelUser user){

    }

    private void initData(HotelUser user) {
        user.setCreateTime(LocalDateTime.now());
    }

    private void storeAdd(HotelUser user){
        insertAllColumn(user);
    }

    private void storeUpdate(HotelUser user){
        updateAllColumnById(user);
    }

    private void storeDelete(long id){
        deleteById(id);
    }

}
