package com.baosight.message.rest.persist.mapper;

import com.baosight.message.rest.persist.po.MessageSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageSourceMapper {

    MessageSource getSource(@Param(value = "sourceCode") String sourceCode);

}
