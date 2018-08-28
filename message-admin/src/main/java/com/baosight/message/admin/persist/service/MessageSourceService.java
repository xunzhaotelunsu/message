package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.mapper.MessageSourceMapper;
import com.baosight.message.admin.persist.po.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageSourceService {

    @Autowired
    MessageSourceMapper messageSourceMapper;

    public int getAmount(){
        return messageSourceMapper.amount();
    }

    public List<MessageSource> queryMessageSource(Map<String, String> param){
        return messageSourceMapper.queryMessageSource(param);
    }

    public boolean check(String sourceCode){
        int count = messageSourceMapper.check(sourceCode);
        if(count==0){
            return true;
        }else{
            return false;
        }
    }

    public MessageSource getMessageSource(String sourceCode){
        Map<String, String> param = new HashMap<>();
        param.put("sourceCode", sourceCode);
        List<MessageSource> list = queryMessageSource(param);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public void modifySource(Map<String, String> param){
        messageSourceMapper.modSource(param);
    }

    public void addMessageSource(MessageSource messageSource){
        messageSourceMapper.insert(messageSource);
    }

    public void deleteSource(String sourceCode){
        messageSourceMapper.delete(sourceCode);
    }
}
