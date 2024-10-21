package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.Image;
import com.it.entity.MessageNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageMapper extends BaseMapper<MessageNotification> {
    @Select("select count(*) from message_notification where is_read=0 and recipient_id=#{userId}")
    int getUnreadCount(String userId);
}
