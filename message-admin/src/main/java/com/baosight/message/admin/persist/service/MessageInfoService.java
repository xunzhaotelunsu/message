package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.mapper.MessageInfoMapper;
import com.baosight.message.admin.persist.po.MessageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MessageInfoService {

    @Autowired
    MessageInfoMapper messageInfoMapper;

    public int todayAmount() {
        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00";
        return messageInfoMapper.amount(today);
    }

    public List<MessageInfo> queryMessageInfo(Map<String, String> param) {
        return messageInfoMapper.queryMessageInfo(param);
    }

    public MessageInfo getMessageInfo(String messageId){
        List<MessageInfo> messageInfoList =  messageInfoMapper.getMessageInfo(messageId);
        if(!CollectionUtils.isEmpty(messageInfoList)){
            return messageInfoList.get(0);
        }
        return null;
    }
}
