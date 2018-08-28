package com.baosight.message.persist.po;

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
