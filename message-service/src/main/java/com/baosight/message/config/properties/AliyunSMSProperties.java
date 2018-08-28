package com.baosight.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by yang on 2018/7/11.
 */
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
public class AliyunSMSProperties {

    String accessKey;

    String secretKey;

    String signName;

}
