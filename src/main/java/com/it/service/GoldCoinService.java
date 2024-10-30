package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.GoldCoin;
import com.it.entity.Image;

public interface GoldCoinService extends IService<GoldCoin> {

    // 获取用户当前的金币数量
    Integer getGoldCoin(String userId);

    // 打赏金币
    void tipping(Integer goldCoin, String userId, String targetUserId, Integer postId,int category);
}
