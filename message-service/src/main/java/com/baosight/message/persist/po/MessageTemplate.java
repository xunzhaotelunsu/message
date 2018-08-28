package com.baosight.message.persist.po;

import lombok.Data;

/**
 * Created by yang on 2018/6/25.
 */
@Data
public class MessageTemplate {

    String messageBizType;

    String messageSendType;

    String template;

    String createTime;
}
