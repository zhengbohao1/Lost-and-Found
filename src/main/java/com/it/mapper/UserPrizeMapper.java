package com.it.mapper;

import com.it.entity.UserPrize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Yu
* @description 针对表【user_prize】的数据库操作Mapper
* @createDate 2024-10-26 13:23:11
* @Entity com.it.entity.UserPrize
*/

@Mapper
public interface UserPrizeMapper extends BaseMapper<UserPrize> {

    /**
     * 获取所有用户兑换奖品的记录信息
     * @return
     */
    @Select("SELECT * FROM user_prize")
    List<UserPrize> selectAll();
}




