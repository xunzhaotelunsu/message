package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.MessageInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by yang on 2018/6/25.
 */
@Mapper
public interface MessageInfoMapper {

    int insert(MessageInfo messageInfo);

}
