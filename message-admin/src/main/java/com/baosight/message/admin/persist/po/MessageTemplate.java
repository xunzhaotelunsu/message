package com.baosight.message.admin.persist.po;

import lombok.Data;

@Data
public class MessageTemplate {

    String messageBizType;

    String messageSendType;

    String template;

    String createTime;
}
