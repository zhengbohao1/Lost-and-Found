package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.LostFound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LostFoundMapper extends BaseMapper<LostFound> {
    @Update("update lost_found set status = 1,claimant_id=#{user_id} where id = #{id}")
    void confirmLost(int id, int user_id);
}
