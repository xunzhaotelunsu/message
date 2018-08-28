package com.baosight.message.sender.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.event.MessageSentEvent;
import com.baosight.message.sender.MessageSender;
import com.baosight.message.service.ContentFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created by yang on 2018/7/16.
 */
//@Component
@Slf4j
public class MobileSender implements MessageSender {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ContentFormatter contentFormatter;

    @Autowired
    JPushClient jPushClient;

    /**
     * 发送message
     *
     * @param message
     * @return
     */
    @Override
    @RabbitListener(containerFactory = "messageSendContainer", queues = MessageConstants.MOBILE)
    public void sendMessage(Message message) throws SenderException {
        //todo 未完成
        log.debug("-----send jpush,messageId:" + message.getMessageId() + ",address:" + message.getMessageReceiverAddress());
        SendResult sendResult = new SendResult();
        sendResult.setMessageSendType(MessageConstants.EMAIL);
        try{
            PushPayload pushPayload = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.alias(message.getMessageReceiverAddress()))
                    .setMessage(cn.jpush.api.push.model.Message.content(message.getContent()))
                    .build();
            PushResult result =  jPushClient.sendPush(pushPayload);
            if(result.isResultOK()){
                sendResult.setSendStatus(true);
            }else{
                sendResult.setSendStatus(false);
                sendResult.setMsg(result.error.getMessage());
                throw new SenderException(result.error.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            sendResult.setSendStatus(false);
            sendResult.setMsg(e.getMessage());
            throw new SenderException(e.getMessage());
        }finally {
            applicationContext.publishEvent(new MessageSentEvent(this, message, sendResult));
        }
    }
}
