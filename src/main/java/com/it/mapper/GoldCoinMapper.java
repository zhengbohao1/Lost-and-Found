package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.GoldCoin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoldCoinMapper extends BaseMapper<GoldCoin> {

    // 根据UserId查询用户金币信息
    @Select("SELECT * FROM gold_coin WHERE user_id = #{userId}")
    GoldCoin selectByUserId(String userId);

    // 根据UserId查询用户金币数量
    @Select("SELECT amount FROM gold_coin WHERE user_id = #{userId}")
    Integer getGoldCoin(String userId);


    // 查看是否已经对该用户打过赏
    @Select("SELECT COUNT(*) FROM tipping_record WHERE user_id = #{userId} AND target_user_id = #{targetUserId} AND post_id = #{postId} AND category = #{category}")
    int checkTipped(String userId, String targetUserId, Integer postId,int category);


    // 增加用户金币数量
    @Update("UPDATE gold_coin SET amount = amount + #{goldCoin} WHERE user_id = #{userId}")
    void increaseGoldCoin(String userId, Integer goldCoin);

    // 减少用户金币数量
    @Update("UPDATE gold_coin SET amount = amount - #{goldCoin} WHERE user_id = #{userId}")
    void decreaseGoldCoin(String userId, Integer goldCoin);

    // 插入打赏记录
    @Insert("INSERT INTO tipping_record (post_id, user_id, target_user_id, category) VALUES (#{postId}, #{userId}, #{targetUserId}, #{category})")
    void insertTippingRecord(Integer postId, String userId, String targetUserId,int category);
}
