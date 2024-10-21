package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MissingNoticesMapper extends BaseMapper<MissingNotices> {
    @Update("update missing_notices set finder_id=#{user_id} where id = #{id}")
    void confirmMissingNotices(int id, String user_id);
}
