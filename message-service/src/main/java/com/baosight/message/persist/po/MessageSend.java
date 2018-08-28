package com.baosight.message.persist.po;

import lombok.Data;

/**
 * Created by yang on 2018/6/25.
 */
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
