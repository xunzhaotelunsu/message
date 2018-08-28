package com.baosight.message.admin.controller.request.info;

import lombok.Data;

@Data
public class ManualRetryInfo {

    String messageId;

    String receiverId;

    String messageSendType;

    String receiverAddress;
}
