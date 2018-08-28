package com.baosight.message.admin.controller.request.server;

import com.baosight.message.core.AbstractPageQuery;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EmailSendServerPageQuery extends AbstractPageQuery {

    String serverCode;

    String personal;

    String active;

    public Map<String, String> toHashMap(){
        Map<String, String> map = new HashMap<>();
        map.put("serverCode", this.serverCode);
        map.put("personal", this.personal);
        map.put("active", this.active);
        return map;
    }
}
