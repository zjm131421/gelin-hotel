package com.gelin.hotel.controller;/**
 * Created by vetech on 2018/11/28.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/28
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
