package com.baosight.message.handler.sent;

import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.core.Receiver;
import com.baosight.message.event.MessageSendingEvent;
import com.baosight.message.event.MessageSentEvent;
import com.baosight.message.sender.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by yang on 2018/6/25.
 */
@Component
@Slf4j
public class InfluxDBSentHandler {

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @EventListener(MessageSentEvent.class)
    @Async
    public void saveMessage(MessageSentEvent event){
        Message message = event.getMessage();
        MessageSender.SendResult sendResult = event.getSendResult();
        final Point point = Point.measurement("message_sent")
                .tag("messageId", message.getMessageId())
                .tag("messageBizType", message.getMessageBizType())
                .tag("messageSource", message.getSourceCode())
                .tag("sendStatus", String.valueOf(sendResult.isSendStatus()))
                .tag("messageSendType", sendResult.getMessageSendType())
                .addField("receiver", message.getMessageReceiver())
                .build();
        try{
            influxDBTemplate.write(point);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
}
