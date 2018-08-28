package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.dto.MessageBizTypeDetail;
import com.baosight.message.admin.persist.po.MessageBizType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageBizTypeMapper {

    int amount();

    int check(@Param(value = "messageBizType") String messageBizType);

    List<MessageBizTypeDetail> queryMessageBizType(Map<String, String> param);

    int insert(MessageBizType messageBizType);

    int update(MessageBizType messageBizType);

    int delete(@Param(value = "messageBizType") String messageBizType);

}
