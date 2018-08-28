package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yang on 2018/6/25.
 */
@Mapper
public interface UserAddressMapper {

    UserAddress getUserAddress(@Param("userId") String userId, @Param("messageSendType") String messageSendType);

    List<UserAddress> getGroupAddress(@Param("groupId") String groupId, @Param("messageSendType") String messageSendType);

}
