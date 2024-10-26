package com.it.service;

import com.it.entity.Prize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.UserPrize;

import java.util.List;

/**
* @author Yu
* @description 针对表【prize】的数据库操作Service
* @createDate 2024-10-09 14:19:16
*/
public interface PrizeService extends IService<Prize> {

    //查询奖品信息
    List<Prize> selectPrizeList();

    //删除奖品
    void deletePrize(Integer id);

    //兑换奖品
    void exchangePrize(Integer id, String userId);

    //管理员更新奖品库存
    void updatePrizeNum(Integer id, Integer num);

    //管理员添加新的奖品
    void addPrize(Prize prize);

    //获取所有用户兑换奖品的记录信息
    List<UserPrize> getUserPrizeList();
}
