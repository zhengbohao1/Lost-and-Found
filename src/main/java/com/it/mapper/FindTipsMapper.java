package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.FindTips;
import com.it.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FindTipsMapper extends BaseMapper<FindTips> {
    @Select("select count(*) from find_tips where is_read=0 and owner_id=#{userId}")
    int getUnreadCount(String userId);
}
