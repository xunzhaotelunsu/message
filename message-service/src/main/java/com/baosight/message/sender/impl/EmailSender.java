package com.baosight.message.sender.impl;

import com.baosight.message.core.AttachmentInfo;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.event.MessageSentEvent;
import com.baosight.message.sender.MessageSender;
import com.baosight.message.sender.extra.ExEmailSender;
import com.baosight.message.sender.holder.ExEmailSenderHolder;
import com.baosight.message.service.ContentFormatter;
import com.baosight.message.service.RateControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.net.URL;

/**
 * Created by yang on 2018/6/19.
 * Email发送器
 */
@Component(value = MessageConstants.EMAIL)
@Slf4j
public class EmailSender implements MessageSender {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ContentFormatter contentFormatter;

    @Autowired
    ExEmailSenderHolder exEmailSenderHolder;

    @Autowired
    RateControlService rateControlService;


    /**
     * 发送message
     *
     * @param message
     * @return
     */
    @Override
    @RabbitListener(containerFactory = "messageSendContainer", queues = MessageConstants.EMAIL)
    public void sendMessage(@Payload Message message) throws SenderException{
        log.debug("-----send email,messageId:" + message.getMessageId() + ",address:" + message.getMessageReceiverAddress());
        SendResult sendResult = new SendResult();
        sendResult.setMessageSendType(MessageConstants.EMAIL);
        String emailAddress = message.getMessageReceiverAddress();
        String title = message.getTitle();
        String content = message.getContent();
        try {
            if(rateControlService.isRateControlled(message.getMessageBizType(), MessageConstants.EMAIL, emailAddress)){
                sendResult.setSendStatus(false);
                sendResult.setMsg("自定义流控");
            }else{
                ExEmailSender exEmailSender = getExEmailSender(message);
                MimeMessage mMessage = exEmailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mMessage, true, "UTF-8");
                helper.setFrom(exEmailSender.getUsername(), exEmailSender.getPersonal());
                helper.setTo(emailAddress);
                helper.setSubject(title);
                helper.setText(contentFormatter.formatContent(message.getMessageBizType(), MessageConstants.EMAIL, content), true);
                //附件处理
                if (!CollectionUtils.isEmpty(message.getAttachments())) {
                    for (AttachmentInfo attachmentInfo : message.getAttachments()) {
                        URL url = new URL(attachmentInfo.getAttUrl());
                        helper.addAttachment(MimeUtility.encodeText(attachmentInfo.getAttName()), new UrlResource(url));
                    }
                }
                exEmailSender.send(mMessage);
                sendResult.setSendStatus(true);
                sendResult.setMsg("sent by " + exEmailSender.getServerCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            sendResult.setSendStatus(false);
            sendResult.setMsg(e.getMessage());
            throw new SenderException(e.getMessage());
        } finally {
            applicationContext.publishEvent(new MessageSentEvent(this, message, sendResult));
        }

    }

    private ExEmailSender getExEmailSender(Message message){
        try{
            Object serverCode = message.getExtraInfo().get("email_serverCode");
            if(!ObjectUtils.isEmpty(serverCode) && !StringUtils.isEmpty(ObjectUtils.getDisplayString(serverCode))){
                ExEmailSender exEmailSender = exEmailSenderHolder.getExEmailSender(ObjectUtils.getDisplayString(serverCode));
                return exEmailSender;
            }
        }catch(Exception e){
            log.debug("switch to highest priority email server");
        }
        return exEmailSenderHolder.getExEmailSenders().get(0);
    }
}
