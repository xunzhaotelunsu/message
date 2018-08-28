package com.baosight.message.rest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by yang on 2018/7/16.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.baosight.message.rest.persist.mapper")
public class MessageRestApplication {

    public static void main(String[] args){
        SpringApplication.run(MessageRestApplication.class, args);
    }
}
