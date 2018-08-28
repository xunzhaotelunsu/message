package com.baosight.message.admin.controller.request.sys;

import com.baosight.message.core.AbstractPageQuery;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageSourcePageQuery extends AbstractPageQuery {

    String sourceCode;

    String sourceName;

    public Map<String, String> toHashMap(){
        Map<String, String> map = new HashMap<>();
        map.put("sourceCode", this.sourceCode);
        map.put("sourceName", this.sourceName);
        return map;
    }

}
