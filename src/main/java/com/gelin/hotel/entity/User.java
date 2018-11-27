package com.gelin.hotel.entity;/**
 * Created by vetech on 2018/11/27.
 */

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.gelin.hotel.entity.base.BaseEntity;

import java.time.LocalDateTime;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@TableName("users")
public class User extends BaseEntity{

    @TableId
    private long id;
    private String username;
    private String password;
    private String name;
    private boolean enable;
    @TableField("create_time")
    private LocalDateTime createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
