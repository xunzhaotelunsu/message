package com.baosight.message.admin.persist.po;

import lombok.Data;

@Data
public class MessageInfo {

    String messageId;

    String sourceCode;

    String messageBizType;

    String messageBizId;

    String messageSender;

    String messageReceiver;

    String messageReceiverType;

    String messageReceiverAddress;

    String title;

    String content;

    String preStatus;

    String remark;

    String privilege;

    String createTime;

    String attachments;

    String extraInfo;

}
