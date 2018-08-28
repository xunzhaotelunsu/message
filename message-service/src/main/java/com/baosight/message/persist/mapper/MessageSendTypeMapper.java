package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.MessageSendType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yang on 2018/6/25.
 */
@Mapper
public interface MessageSendTypeMapper {

    List<MessageSendType> selectSendTypeByBizType(@Param("messageBizType") String messageBizType);
}
