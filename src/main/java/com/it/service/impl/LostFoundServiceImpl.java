package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Advises;
import com.it.mapper.LostFoundMapper;
import com.it.entity.LostFound;
import com.it.service.LostFoundService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Override
    public List<LostFound> legalList() {
        List<LostFound> lostFounds = this.list();
        //获取审核成功的lostfounds
        return lostFounds.stream().filter(lostFound -> lostFound.getReviewProcess() == 1).toList();
    }

    @Override
    public List<LostFound> selectByCondition(String content) {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("item_name",content);
        return baseMapper.selectList(queryWrapper);
    }
}
