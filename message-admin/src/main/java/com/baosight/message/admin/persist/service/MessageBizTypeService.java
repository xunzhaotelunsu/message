package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.dto.MessageBizTypeDetail;
import com.baosight.message.admin.persist.mapper.MessageBizTypeMapper;
import com.baosight.message.admin.persist.mapper.MessageInfoMapper;
import com.baosight.message.admin.persist.mapper.MessageSendTypeMapper;
import com.baosight.message.admin.persist.mapper.MessageTemplateMapper;
import com.baosight.message.admin.persist.po.MessageBizType;
import com.baosight.message.admin.persist.po.MessageSendType;
import com.baosight.message.admin.persist.po.MessageTemplate;
import com.baosight.message.admin.persist.po.SendType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageBizTypeService {

    @Autowired
    MessageBizTypeMapper messageBizTypeMapper;

    @Autowired
    MessageSendTypeMapper messageSendTypeMapper;

    @Autowired
    MessageTemplateMapper messageTemplateMapper;

    @Autowired
    MessageInfoMapper messageInfoMapper;

    public int getAmount(){
        return messageBizTypeMapper.amount();
    }

    public boolean check(String messageBizType){
        int count = messageBizTypeMapper.check(messageBizType);
        if(count==0){
            return true;
        }else{
            return false;
        }
    }

    public void addMessageBizType(MessageBizType messageBizType){
        messageBizTypeMapper.insert(messageBizType);
    }

    public List<MessageBizTypeDetail> queryMessageBizType(Map<String, String> param){
        return messageBizTypeMapper.queryMessageBizType(param);
    }

    public MessageBizTypeDetail getMessageBizType(String messageBizType){
        Map<String, String> param = new HashMap<>();
        param.put("messageBizType", messageBizType);
        List<MessageBizTypeDetail> messageBizTypeDetails = queryMessageBizType(param);
        if(CollectionUtils.isEmpty(messageBizTypeDetails)){
            return null;
        }else{
            return messageBizTypeDetails.get(0);
        }
    }

    public List<SendType> getSendTypes(String messageBizType){
        return messageSendTypeMapper.queryMessageSendTypes(messageBizType);
    }

    public void updateMessageBizType(MessageBizType messageBizType){
        messageBizTypeMapper.update(messageBizType);
    }

    public String getTemplate(String messageBizType, String messageSendType){
        MessageTemplate messageTemplate = messageTemplateMapper.getTemplate(messageBizType, messageSendType);
        if(ObjectUtils.isEmpty(messageTemplate)){
            return "";
        }else{
            return messageTemplate.getTemplate();
        }
    }

    public void setMessageTemplate(MessageTemplate template){
        MessageTemplate messageTemplate = messageTemplateMapper.getTemplate(template.getMessageBizType(), template.getMessageSendType());
        if(ObjectUtils.isEmpty(messageTemplate)){
            //插入
            messageTemplateMapper.insert(template);
        }else{
            if(StringUtils.hasText(template.getTemplate())){
                //更新
                messageTemplateMapper.update(template);
            }else{//删除
                MessageSendType messageSendType = new MessageSendType();
                BeanUtils.copyProperties(template, messageSendType);
                messageTemplateMapper.delete(messageSendType);
            }
        }
    }

    public void addMessageSendTypes(String messageBizType, List<String> sendTypes){
        sendTypes.stream().forEach(str ->{
            MessageSendType tmp = new MessageSendType();
            tmp.setMessageBizType(messageBizType);
            tmp.setMessageSendType(str);
            messageSendTypeMapper.insert(tmp);
        });
    }

    public void deleteMessageSendTypes(String messageBizType, List<String> sendTypes){
        sendTypes.stream().forEach(str ->{
            MessageSendType tmp = new MessageSendType();
            tmp.setMessageBizType(messageBizType);
            tmp.setMessageSendType(str);
            messageSendTypeMapper.delete(tmp);
            messageTemplateMapper.delete(tmp);
        });
    }

    /**
     * 删除消息业务类型（删除关联的消息、消息发送、消息发送类型、消息模板）
     * @param messageBizType
     */
    public void deleteMessageBizType(String messageBizType){
        messageInfoMapper.deleteByBizType(messageBizType);
        messageTemplateMapper.deleteByBizType(messageBizType);
        messageSendTypeMapper.deleteByBizType(messageBizType);
        messageBizTypeMapper.delete(messageBizType);
    }
}
