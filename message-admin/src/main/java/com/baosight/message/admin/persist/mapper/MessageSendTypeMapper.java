package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.MessageSendType;
import com.baosight.message.admin.persist.po.SendType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageSendTypeMapper {

    List<SendType> queryMessageSendTypes(@Param(value = "messageBizType") String messageBizType);

    int insert(MessageSendType messageSendType);

    int delete(MessageSendType messageSendType);

    int deleteByBizType(@Param(value = "messageBizType") String messageBizType);
}
