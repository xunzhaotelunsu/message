package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.mapper.MessageSendMapper;
import com.baosight.message.admin.persist.po.MessageSend;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MessageSendService {

    @Autowired
    MessageSendMapper messageSendMapper;

    public int todayAmount(){
        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00";
        return messageSendMapper.amount(today);
    }

    public List<MessageSend> queryMessageSend(Map<String, String> param){
        return messageSendMapper.queryMessageSend(param);
    }
}
