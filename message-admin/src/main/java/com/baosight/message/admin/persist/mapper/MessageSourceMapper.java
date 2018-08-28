package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.MessageSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageSourceMapper {

    int amount();

    int check(@Param(value = "sourceCode") String sourceCode);

    List<MessageSource> queryMessageSource(Map<String, String> param);

    int insert(MessageSource messageSource);

    int modSource(Map<String, String> param);

    int delete(@Param(value = "sourceCode") String sourceCode);
}
