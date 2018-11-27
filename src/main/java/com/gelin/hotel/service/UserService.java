package com.gelin.hotel.service;/**
 * Created by vetech on 2018/11/27.
 */

import com.gelin.hotel.core.BaseServiceImpl;
import com.gelin.hotel.entity.User;
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
public class UserService extends BaseServiceImpl<UserMapper,User>{

    public User require(long id){
        return getById(id).orElseThrow(() -> new RuntimeException(String.format("未查询到用户信息：%d",id)));
    }

    public Optional<User> getById(long id){
        return Optional.ofNullable(selectById(id));
    }

    public User add(User user){
        initData(user);
        storeAdd(user);
        return user;
    }

    public User update(User user){
        doUpdate(user);
        storeUpdate(user);
        return user;
    }

    public User enable(User user,boolean enable){
        user.setEnable(enable);
        storeUpdate(user);
        return user;
    }

    private void doUpdate(User user){

    }

    private void initData(User user) {
        user.setCreateTime(LocalDateTime.now());
    }

    private void storeAdd(User user){
        insertAllColumn(user);
    }

    private void storeUpdate(User user){
        updateAllColumnById(user);
    }

    private void storeDelete(long id){
        deleteById(id);
    }

}
