package com.baosight.message.handler.pre;

import com.baosight.message.core.Message;
import com.baosight.message.handler.MessagePreHandleException;
import com.baosight.message.handler.MessagePreHandler;
import com.baosight.message.service.SensitiveWordsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by yang on 2018/6/19.
 */
@Component
@Order(4)
public class SensitiveWordsHandler implements MessagePreHandler {

    @Autowired
    SensitiveWordsFilter sensitiveWordsFilter;

    /**
     * 预处理消息
     * 敏感词处理（内容、标题）
     * @param message
     * @return
     */
    @Override
    public Message handleMessage(Message message) throws MessagePreHandleException {
        String content = message.getContent();
        String title = message.getTitle();
        content = sensitiveWordsFilter.filterText(content);
        title = sensitiveWordsFilter.filterText(title);
        message.setContent(content);
        message.setTitle(title);
        return message;
    }
}
