package com.baosight.message.admin.controller.request.biz;

import com.baosight.message.core.AbstractPageQuery;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageBizTypePageQuery extends AbstractPageQuery {

    String sourceCode;

    String messageBizType;

    public Map<String, String> toHashMap(){
        Map<String, String> map = new HashMap<>();
        map.put("sourceCode", this.sourceCode);
        map.put("messageBizType", this.messageBizType);
        return map;
    }
}
