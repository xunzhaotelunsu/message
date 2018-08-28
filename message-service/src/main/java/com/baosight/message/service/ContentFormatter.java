package com.baosight.message.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baosight.message.persist.mapper.MessageTemplateMapper;
import com.baosight.message.persist.po.MessageTemplate;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by yang on 2018/6/26.
 * 内容格式化
 */
@Component
public class ContentFormatter {

    @Autowired
    MessageTemplateMapper messageTemplateMapper;

    public String getTemplate(String messageBizType, String messageSendType){
        MessageTemplate messageTemplate = messageTemplateMapper.getTemplate(messageBizType, messageSendType);
        if(!ObjectUtils.isEmpty(messageTemplate)){
            return messageTemplate.getTemplate();
        }
        return null;
    }

    public String formatContent(String messageBizType, String messageSendType, String content){
        String template =  getTemplate(messageBizType, messageSendType);
        if(StringUtils.hasText(template)){
            Map<String, Object> param = JSON.parseObject(content, new TypeReference<Map<String, Object>>() {});
            // 开始格式化
            VelocityEngine ve = new VelocityEngine();
            ve.init();
            // 把数据填入上下文
            VelocityContext context = new VelocityContext(param);
            // 输出流
            StringWriter writer = new StringWriter();
            // 转换输出
            ve.evaluate(context, writer, "", template);
            return writer.toString();
        }else{
            return content;
        }
    }
}
