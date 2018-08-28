package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.MessageSendType;
import com.baosight.message.admin.persist.po.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageTemplateMapper {

    MessageTemplate getTemplate(@Param("messageBizType") String messageBizType, @Param("messageSendType") String messageSendType);

    int delete(MessageSendType messageSendType);

    int deleteByBizType(@Param("messageBizType") String messageBizType);

    int insert(MessageTemplate messageTemplate);

    int update(MessageTemplate messageTemplate);
}
