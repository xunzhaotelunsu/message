package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yang on 2018/6/25.
 */
@Mapper
public interface MessageTemplateMapper {

    MessageTemplate getTemplate(@Param("messageBizType") String messageBizType, @Param("messageSendType") String messageSendType);
}
