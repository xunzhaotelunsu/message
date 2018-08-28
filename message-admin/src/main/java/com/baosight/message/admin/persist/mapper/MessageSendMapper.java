package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.MessageSend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageSendMapper {

    int amount(@Param(value = "today") String today);

    List<MessageSend> queryMessageSend(Map<String, String> param);
}
