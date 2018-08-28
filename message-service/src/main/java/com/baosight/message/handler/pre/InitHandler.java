package com.baosight.message.handler.pre;

import com.baosight.message.core.Message;
import com.baosight.message.handler.MessagePreHandleException;
import com.baosight.message.handler.MessagePreHandler;
import com.baosight.message.persist.mapper.MessageBizTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by yang on 2018/6/22.
 */
@Component
@Order(1)
public class InitHandler implements MessagePreHandler {

    @Autowired
    MessageBizTypeMapper messageBizTypeMapper;
    /**
     * 预处理消息
     * 检查
     * @param message
     * @return
     */
    @Override
    public Message handleMessage(Message message) throws MessagePreHandleException {
        int count = messageBizTypeMapper.check(message.getSourceCode(), message.getMessageBizType());
        if(count < 1){
            throw new MessagePreHandleException("错误的消息业务类型");
        }
        return message;
    }
}
