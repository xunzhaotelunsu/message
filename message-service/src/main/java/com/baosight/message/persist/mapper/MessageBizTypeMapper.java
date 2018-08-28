package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.MessageBizType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageBizTypeMapper {

    MessageBizType getMessageBizType(@Param(value = "messageBizType") String messageBizType);

    int check(@Param(value = "sourceCode")String sourceCode,@Param(value = "messageBizType") String messageBizType);
}
