package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.MessageSend;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by yang on 2018/6/25.
 */
@Mapper
public interface MessageSendMapper {

    int insert(MessageSend messageSend);

    int setSendStatus(Map<String, String> param);

}
