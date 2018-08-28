package com.baosight.message.admin.persist.po;

import lombok.Data;

@Data
public class MessageBizType {

    String messageBizType;

    String sourceCode;

    String typeName;

    int rateLimit;

    String limitUnit;

    String createTime;
}
