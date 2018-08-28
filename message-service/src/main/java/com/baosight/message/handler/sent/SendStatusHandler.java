package com.baosight.message.handler.sent;

import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.event.MessageSentEvent;
import com.baosight.message.persist.mapper.MessageSendMapper;
import com.baosight.message.sender.MessageSender;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yang on 2018/6/25.
 * 修改消息发送状态
 */
@Component
public class SendStatusHandler {

    @Autowired
    MessageSendMapper messageSendMapper;

    @EventListener(MessageSentEvent.class)
    @Async
    @Transactional
    public void setSendStatus(MessageSentEvent event) {
        Message message = event.getMessage();
        MessageSender.SendResult sendResult = event.getSendResult();
        Map<String, String> param = new HashMap<>();
        param.put("messageId", message.getMessageId());
        param.put("receiverId", message.getMessageReceiver());
        param.put("messageSendType", sendResult.getMessageSendType());
        if(sendResult.isSendStatus()){
            param.put("sendStatus", MessageConstants.SEND_SUCCESS);
            param.put("remark", sendResult.getMsg());
        }else{
            param.put("sendStatus", MessageConstants.SEND_FAILED);
            param.put("remark", sendResult.getMsg());
        }
        param.put("sendTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        messageSendMapper.setSendStatus(param);
    }
}
