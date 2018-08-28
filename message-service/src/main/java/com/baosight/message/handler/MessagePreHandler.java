package com.baosight.message.handler;

import com.baosight.message.core.Message;

/**
 * Created by yang on 2018/6/19.
 */
public interface MessagePreHandler {

    /**
     * 预处理消息
     * @param message
     * @return
     */
    Message handleMessage(Message message) throws MessagePreHandleException;

}
