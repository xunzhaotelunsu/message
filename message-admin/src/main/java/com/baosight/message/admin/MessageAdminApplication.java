package com.baosight.message.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by yang on 2018/7/2.
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.baosight.message.admin.persist.mapper"})
@EnableTransactionManagement
public class MessageAdminApplication {

    public static void main(String[] args){
        SpringApplication.run(MessageAdminApplication.class, args);
    }
}
