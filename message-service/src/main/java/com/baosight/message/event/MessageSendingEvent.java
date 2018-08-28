package com.baosight.message.event;

import com.baosight.message.core.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * Created by yang on 2018/6/19.
 * message预备发送事件
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageSendingEvent extends ApplicationEvent {

    Message message;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageSendingEvent(Object source, Message message) {
        super(source);
        setMessage(message);
    }
}
