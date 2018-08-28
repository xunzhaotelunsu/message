package com.baosight.message.handler.send;

import com.alibaba.fastjson.JSON;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.core.Receiver;
import com.baosight.message.event.MessageSendingEvent;
import com.baosight.message.persist.mapper.MessageInfoMapper;
import com.baosight.message.persist.mapper.MessageSendMapper;
import com.baosight.message.persist.po.MessageInfo;
import com.baosight.message.persist.po.MessageSend;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Created by yang on 2018/6/19.
 * 消息存储、分发
 */
@Component
public class DispatchHandler {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MessageInfoMapper messageInfoMapper;

    @Autowired
    MessageSendMapper messageSendMapper;

    @EventListener(MessageSendingEvent.class)
    @Async
    @Transactional
    public void dispatchMessage(MessageSendingEvent event) {
        Message message = event.getMessage();
        //persist message
        MessageInfo messageInfo = new MessageInfo();
        BeanUtils.copyProperties(message, messageInfo);
        if(!CollectionUtils.isEmpty(message.getAttachments())){
            messageInfo.setAttachments(JSON.toJSONString(message.getAttachments()));
        }
        if(!CollectionUtils.isEmpty(message.getExtraInfo())){
            messageInfo.setExtraInfo(JSON.toJSONString(message.getExtraInfo()));
        }
        messageInfoMapper.insert(messageInfo);
        //dispatch
        if (!MessageConstants.PRE_FAILED.equals(message.getPreStatus())) {//预处理成功，可以发送
            for (Receiver receiver : message.getReceiverList()) {
                Message single = new Message();
                BeanUtils.copyProperties(message, single, "receiverList", "messageSendType", "messageReceiverType");
                single.setMessageReceiver(receiver.getReceiverId());
                single.setMessageReceiverAddress(receiver.getReceiverAddress());
                //persist message_send
                MessageSend messageSend = new MessageSend();
                messageSend.setMessageId(message.getMessageId());
                messageSend.setMessageSendType(receiver.getMessageSendType());
                messageSend.setReceiverId(receiver.getReceiverId());
                messageSend.setReceiverAddress(receiver.getReceiverAddress());
                messageSend.setSendStatus(MessageConstants.FOR_SEND);
                messageSendMapper.insert(messageSend);
                //send to mq
                rabbitTemplate.convertAndSend(receiver.getMessageSendType(), single);
            }
        }
    }
}
