package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.Advises;
import com.it.entity.ClaimRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClaimRequestMapper extends BaseMapper<ClaimRequest> {
    @Select("select count(*) from claim_request where read_status=0 and finder_id=#{userId}")
    int getUnreadCount(int userId);
}
