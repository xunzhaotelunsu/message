package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageInfoMapper {

    int amount(@Param(value = "today") String today);

    List<MessageInfo> queryMessageInfo(Map<String, String> param);

    List<MessageInfo> getMessageInfo(@Param(value = "messageId") String messageId);

    int deleteByBizType(@Param(value = "messageBizType") String messageBizType);
}
