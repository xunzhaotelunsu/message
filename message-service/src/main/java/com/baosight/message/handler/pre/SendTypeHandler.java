package com.baosight.message.handler.pre;

import com.baosight.message.core.Message;
import com.baosight.message.handler.MessagePreHandleException;
import com.baosight.message.handler.MessagePreHandler;
import com.baosight.message.persist.mapper.MessageSendTypeMapper;
import com.baosight.message.persist.po.MessageSendType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2018/6/25.
 */
@Component
@Order(2)
public class SendTypeHandler implements MessagePreHandler{

    @Autowired
    MessageSendTypeMapper messageSendTypeMapper;

    /**
     * 预处理消息
     * 处理消息发送类型
     * @param message
     * @return
     */
    @Override
    public Message handleMessage(Message message) throws MessagePreHandleException {
        String messageBizType = message.getMessageBizType();
        //根据消息业务类型获取发送类型
        List<MessageSendType> list = messageSendTypeMapper.selectSendTypeByBizType(messageBizType);
        if (CollectionUtils.isEmpty(list)) {
            throw new MessagePreHandleException("无指定的消息发送类型");
        }else{
            List<String> messageSendTypeList = new ArrayList<>();
            for(MessageSendType messageSendType:list){
                messageSendTypeList.add(messageSendType.getMessageSendType());
            }
            message.setMessageSendType(messageSendTypeList);
        }
        return message;
    }
}
