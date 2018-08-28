package com.baosight.message.admin.controller.request.info;

import com.baosight.message.core.AbstractPageQuery;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageInfoPageQuery extends AbstractPageQuery {

    String sourceCode;

    String messageBizType;

    String messageReceiver;

    String startTime;

    String endTime;

    public Map<String,String> toHashMap(){
        Map<String,String> map = new HashMap<>();
        map.put("sourceCode", this.sourceCode);
        map.put("messageBizTyp", this.messageBizType);
        map.put("messageReceiver", this.messageReceiver);
        map.put("startTime", this.startTime);
        map.put("endTime", this.endTime);
        return map;
    }

}
