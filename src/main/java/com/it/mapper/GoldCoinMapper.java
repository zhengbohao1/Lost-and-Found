package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.GoldCoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoldCoinMapper extends BaseMapper<GoldCoin> {

    // 根据UserId查询用户金币信息
    @Select("SELECT * FROM gold_coin WHERE user_id = #{userId}")
    GoldCoin selectByUserId(String userId);
}
