package com.baosight.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by yang on 2018/6/19.
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.baosight.message.persist.mapper"})
public class MessageServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(MessageServiceApplication.class, args);
    }
}
