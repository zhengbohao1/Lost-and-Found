package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Prize;
import com.it.entity.UserPrize;
import com.it.exception.BusinessException;
import com.it.mapper.GoldCoinMapper;
import com.it.mapper.UserPrizeMapper;
import com.it.service.PrizeService;
import com.it.mapper.PrizeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Yu
* @description 针对表【prize】的数据库操作Service实现
* @createDate 2024-10-09 14:19:16
*/
@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize>
    implements PrizeService{

    @Resource
    private PrizeMapper prizeMapper;

    @Resource
    private GoldCoinMapper goldCoinMapper;

    @Resource
    private UserPrizeMapper userPrizeMapper;

    /**
     * 查询全部奖品信息
     * @return
     */
    @Override
    public List<Prize> selectPrizeList() {
        return this.baseMapper.selectPrizeList();
    }

    /**
     * 根据id删除奖品
     * @param id
     */
    @Override
    public void deletePrize(Integer id) {
        this.prizeMapper.deleteById(id);
    }

    /**
     * 根据id兑换奖品
     * @param id
     * @param userId
     */
    @Override
    @Transactional
    public void exchangePrize(Integer id, String userId) {
        // TODO: 扣除用户金币
        int prizePrice = this.prizeMapper.selectById(id).getPrizePrice();
        if(goldCoinMapper.selectByUserId(userId).getAmount() < prizePrice){
            throw new BusinessException("您的金币不足！");
        }
        this.prizeMapper.deductUserCoin(userId, prizePrice);
        // TODO: 奖品的库存减1
        if(prizeMapper.selectById(id).getPrizeQuantity() == 0){
            throw new BusinessException("奖品的库存不足！");
        }
        this.prizeMapper.deductPrizeNum(id);
        // TODO: 增加用户奖品
        // 获取id对应的奖品名称
        String prizeName = this.prizeMapper.selectById(id).getPrizeName();
        this.prizeMapper.addUserPrize(userId, prizeName);
    }

    /**
     * 根据id更新奖品库存
     * @param id
     * @param num
     */
    @Override
    public void updatePrizeNum(Integer id, Integer num) {
        this.prizeMapper.updatePrizeNum(id, num);
    }

    /**
     * 管理员添加新的奖品
     * @param prize
     */
    @Override
    public void addPrize(Prize prize) {
        this.prizeMapper.insert(prize);
    }

    /**
     * 获取所有用户兑换奖品的记录信息
     * @return
     */
    @Override
    public List<UserPrize> getUserPrizeList() {
        return userPrizeMapper.selectAll();
    }
}




