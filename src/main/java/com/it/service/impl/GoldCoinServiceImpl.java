package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.GoldCoin;
import com.it.entity.Image;
import com.it.entity.MessageNotification;
import com.it.enums.MessageType;
import com.it.exception.BusinessException;
import com.it.mapper.GoldCoinMapper;
import com.it.mapper.ImageMapper;
import com.it.service.GoldCoinService;
import com.it.service.ImageService;
import com.it.service.MessageNotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoldCoinServiceImpl extends ServiceImpl<GoldCoinMapper, GoldCoin> implements GoldCoinService {

    @Resource
    private GoldCoinMapper goldCoinMapper;

    /**
     * 获取用户当前的金币数量
     * @param userId
     * @return
     */
    @Override
    public Integer getGoldCoin(String userId) {
        return goldCoinMapper.getGoldCoin(userId);
    }

    @Resource
    private MessageNotificationService messageNotificationService;
    /**
     * 打赏金币
     * @param goldCoin
     * @param userId
     * @param targetUserId
     */
    @Transactional
    @Override
    public void tipping(Integer goldCoin, String userId, String targetUserId, Integer postId,int category) {
        // TODO: 查看是否已经对该用户打过赏
        int rows = goldCoinMapper.checkTipped(userId, targetUserId, postId, category);
        if(rows > 0){
            throw new BusinessException("已经对该用户打过赏了");
        }
        // TODO: 查看打赏人的金币数量是否足够
        int userGoldCoin = goldCoinMapper.getGoldCoin(userId);
        if(userGoldCoin < goldCoin){
            throw new BusinessException("金币数量不足");
        }
        // TODO: 进行打赏
        goldCoinMapper.increaseGoldCoin(targetUserId, goldCoin);
        goldCoinMapper.decreaseGoldCoin(userId, goldCoin);
        // TODO: 添加打赏记录
        goldCoinMapper.insertTippingRecord(postId, userId, targetUserId,category);
        // TODO: 发消息
        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setRecipientId(targetUserId);
        messageNotification.setMessageType(MessageType.GOLD_DASHANG);
        messageNotification.setMessageContent("您有一条打赏记录，获得"+goldCoin+"个金币！请点击查看~");
        messageNotification.setIsRead(0);
        messageNotification.setSenderId(userId);
        messageNotificationService.save(messageNotification);
    }


}
