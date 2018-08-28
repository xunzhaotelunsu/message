package com.baosight.message.sender.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.baosight.message.config.properties.AliyunSMSProperties;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.event.MessageSentEvent;
import com.baosight.message.sender.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by yang on 2018/6/26.
 * SMS发送器
 */
@Component(value = MessageConstants.SMS)
@Slf4j
public class SMSSender implements MessageSender {

    @Autowired
    AliyunSMSProperties aliyunSMSProperties;

    @Autowired
    IAcsClient iAcsClient;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 发送message
     *
     * @param message
     * @return
     */
    @Override
    @RabbitListener(containerFactory = "messageSendContainer", queues = MessageConstants.SMS)
    public void sendMessage(Message message) throws SenderException {
        log.debug("-----send sms,messageId:" + message.getMessageId() + ",address:" + message.getMessageReceiverAddress());
        SendResult sendResult = new SendResult();
        sendResult.setMessageSendType(MessageConstants.SMS);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setSignName(aliyunSMSProperties.getSignName());
        request.setTemplateCode("SMS_126700017");
        request.setTemplateParam(message.getContent());
        request.setPhoneNumbers(message.getMessageReceiverAddress());
        try {
            SendSmsResponse sendSmsResponse = iAcsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                sendResult.setSendStatus(true);
                sendResult.setMsg("sent by aliyun");
            }else{
                sendResult.setSendStatus(false);
                sendResult.setMsg(sendSmsResponse.getMessage());
                throw new SenderException(sendSmsResponse.getMessage());
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
