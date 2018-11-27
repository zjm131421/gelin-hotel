package com.gelin.hotel;

import com.gelin.hotel.helper.ApplicationContextHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;


//默认注入Spring对象获取工具
@Import({ApplicationContextHelper.class})
@EnableAutoConfiguration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GelinHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(GelinHotelApplication.class, args);
    }
}
