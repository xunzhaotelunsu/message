package com.baosight.message.event;

import com.baosight.message.core.Message;
import com.baosight.message.sender.MessageSender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * Created by yang on 2018/6/20.
 * message发送完成（成功or失败）事件
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageSentEvent extends ApplicationEvent{

    Message message;

    MessageSender.SendResult sendResult;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageSentEvent(Object source, Message message, MessageSender.SendResult sendResult) {
        super(source);
        setMessage(message);
        setSendResult(sendResult);
    }
}
