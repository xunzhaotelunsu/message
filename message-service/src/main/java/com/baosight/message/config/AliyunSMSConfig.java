package com.baosight.message.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baosight.message.config.properties.AliyunSMSProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yang on 2018/7/11.
 */
@Configuration
@EnableConfigurationProperties({AliyunSMSProperties.class})
@Slf4j
public class AliyunSMSConfig {

    @Autowired
    AliyunSMSProperties aliyunSMSProperties;

    @Bean
    public IAcsClient getSMSSender(){
        IAcsClient acsClient = null;
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunSMSProperties.getAccessKey(),
                aliyunSMSProperties.getSecretKey());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            acsClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        }
        return acsClient;
    }

}
