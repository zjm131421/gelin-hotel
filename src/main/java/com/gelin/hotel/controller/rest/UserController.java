package com.gelin.hotel.controller.rest;/**
 * Created by vetech on 2018/11/27.
 */

import com.gelin.hotel.core.api.RestResponse;
import com.gelin.hotel.entity.HotelUser;
import com.gelin.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public RestResponse add(@RequestBody HotelUser data){
        return new RestResponse<>(userService.add(data));
    }

    @GetMapping("/get")
    public RestResponse get(){
        return new RestResponse<>("success");
    }


}
