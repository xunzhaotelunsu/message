package com.baosight.message.sender;

import com.baosight.message.core.Message;
import lombok.Data;

/**
 * Created by yang on 2018/6/19.
 */
public interface MessageSender {

    /**
     * 发送message
     * @param message
     * @return
     */
    void sendMessage(Message message) throws SenderException;

    class SenderException extends Exception{
        public SenderException(String msg){
            super(msg);
        }
    }

    @Data
    class SendResult{

        boolean sendStatus;

        String msg;

        String messageSendType;

    }
}
