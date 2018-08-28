package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.SendType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SendTypeMapper {

    List<SendType> sendTypes();
}
