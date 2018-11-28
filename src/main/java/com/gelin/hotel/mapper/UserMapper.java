package com.gelin.hotel.mapper;/**
 * Created by vetech on 2018/11/27.
 */

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gelin.hotel.entity.HotelUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@Mapper
public interface UserMapper extends BaseMapper<HotelUser>{

    HotelUser getByUsername(@Param("username")String username);

}
