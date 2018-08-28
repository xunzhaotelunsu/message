package com.baosight.message.handler.send;

import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.core.Receiver;
import com.baosight.message.event.MessageSendingEvent;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by yang on 2018/6/21.
 */
@Component
@Slf4j
public class InfluxDBHandler {

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @EventListener(MessageSendingEvent.class)
    @Async
    public void sendToInfluxDB(MessageSendingEvent event) {
        Message message = event.getMessage();
        if (!MessageConstants.PRE_FAILED.equals(message.getPreStatus())) {//可以发送
            for (Receiver receiver : message.getReceiverList()) {
                Point point = Point.measurement("message_sending")
                        .tag("messageId", message.getMessageId())
                        .tag("messageBizType", message.getMessageBizType())
                        .tag("messageSendType", receiver.getMessageSendType())
                        .tag("messageSource", message.getSourceCode())
                        .addField("receiver", receiver.getReceiverId())
                        .addField("receiverAddress", receiver.getReceiverAddress())
                        .build();
                try {
                    influxDBTemplate.write(point);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        }
    }
}
