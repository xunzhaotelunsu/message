package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.mapper.EmailSendServerMapper;
import com.baosight.message.admin.persist.po.EmailSendServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmailSendServerService {

    @Autowired
    EmailSendServerMapper emailSendServerMapper;

    public List<EmailSendServer> queryEmailSendServer(Map<String, String> param){
        return emailSendServerMapper.queryEmailSendServer(param);
    }

    public EmailSendServer getEmailSendServer(String serverCode){
        Map<String, String> param = new HashMap<>();
        param.put("serverCode", serverCode);
        List<EmailSendServer> emailSendServers = queryEmailSendServer(param);
        if (CollectionUtils.isEmpty(emailSendServers)) {
            return null;
        }else{
            return emailSendServers.get(0);
        }
    }

    public boolean check(String serverCode){
        int count = emailSendServerMapper.check(serverCode);
        if(count == 0){
            return true;
        }else{
            return false;
        }
    }

    public void update(EmailSendServer emailSendServer){
        emailSendServerMapper.update(emailSendServer);
    }

    public void insert(EmailSendServer emailSendServer){
        emailSendServerMapper.insert(emailSendServer);
    }

    public void delete(String serverCode){
        emailSendServerMapper.delete(serverCode);
    }

    public void setActive(String serverCode, String status){
        if ("true".equals(status)) {
            emailSendServerMapper.active(serverCode);
        } else{
            emailSendServerMapper.inactive(serverCode);
        }
    }

}
