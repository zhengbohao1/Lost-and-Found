package com.it.mapper;

import com.it.entity.Prize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author Yu
* @description 针对表【prize】的数据库操作Mapper
* @createDate 2024-10-09 14:19:16
* @Entity com.it.entity.Prize
*/

@Mapper
public interface PrizeMapper extends BaseMapper<Prize> {

    //查询全部奖品信息
    @Select("SELECT * FROM prize")
    List<Prize> selectPrizeList();

    //扣除用户金币
    @Update("UPDATE gold_coin SET amount = amount - #{prizePrice} WHERE user_id = #{userId}")
    void deductUserCoin(String userId, int prizePrice);

    //增加用户奖品
    @Insert("INSERT INTO user_prize (user_id, prize_name,exchanged_time) VALUES (#{userId}, #{prizeName},now())")
    void addUserPrize(String userId, String prizeName);

    //增加奖品库存
    @Update("UPDATE prize SET prize_quantity = #{num} WHERE id = #{id}")
    void updatePrizeNum(Integer id, Integer num);

    //奖品的库存减1
    @Update("UPDATE prize SET prize_quantity = prize_quantity - 1 WHERE id = #{id}")
    void deductPrizeNum(Integer id);
}




