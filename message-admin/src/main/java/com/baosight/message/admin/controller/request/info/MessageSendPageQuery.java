package com.baosight.message.admin.controller.request.info;

import com.baosight.message.core.AbstractPageQuery;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageSendPageQuery extends AbstractPageQuery {

    String messageId;

    String receiverId;

    String messageSendType;

    String sendStatus;

    public Map<String, String> toHashMap(){
        Map<String, String> map = new HashMap<>();
        map.put("messageId", this.messageId);
        map.put("receiverId", this.receiverId);
        map.put("messageSendType", this.messageSendType);
        map.put("sendStatus", this.sendStatus);
        return map;
    }
}
