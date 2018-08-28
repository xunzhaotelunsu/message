package com.baosight.message.admin.persist.po;

import lombok.Data;

@Data
public class MessageSend {

    String messageId;

    String receiverId;

    String messageSendType;

    String receiverAddress;

    String sendStatus;

    String sendTime;

    String readStatus;

    String readTime;

    String remark;
}
