package com.baosight.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.event.MessageSendingEvent;
import com.baosight.message.handler.MessagePreHandleException;
import com.baosight.message.handler.MessagePreHandler;
import com.baosight.message.sender.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by yang on 2018/6/19.
 */
@Service
@Slf4j
public class MessageService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    List<MessagePreHandler> messagePreHandlers;

    @RabbitListener(containerFactory = "messageCollectorContainer" , queues = {MessageConstants.MESSAGE_QUEUE})
    public void processMessage(org.springframework.amqp.core.Message amqpMessage){
        String messageBody = new String(amqpMessage.getBody());
        Message message = null;
        try{
            message = JSON.parseObject(messageBody, Message.class);
        }catch(Exception e){
            log.error(e.getMessage());
            return;
        }
        for(MessagePreHandler messagePreHandler:messagePreHandlers){
            try {
                message = messagePreHandler.handleMessage(message);
            } catch (MessagePreHandleException | RuntimeException e) {
                message.setPreStatus(MessageConstants.PRE_FAILED);
                message.setRemark(e.getMessage());
                break;
            }
        }
        if(!MessageConstants.PRE_FAILED.equals(message.getPreStatus())){
            message.setPreStatus(MessageConstants.PRE_SUCCESS);
        }
        MessageSendingEvent messageSendingEvent = new MessageSendingEvent(this, message);
        applicationContext.publishEvent(messageSendingEvent);
    }

    @RabbitListener(containerFactory = "messageCollectorContainer" , queues = {MessageConstants.MANUAL_QUEUE})
    public void manualRetry(org.springframework.amqp.core.Message amqpMessage){
        String messageBody = new String(amqpMessage.getBody());
        Message message = null;
        try{
            message = JSON.parseObject(messageBody, Message.class);
        }catch(Exception e){
            log.error(e.getMessage());
            return;
        }
        try{
            String messageSendType = message.getMessageSendType().get(0);
            MessageSender messageSender = (MessageSender) applicationContext.getBean(messageSendType);
            messageSender.sendMessage(message);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }


}
