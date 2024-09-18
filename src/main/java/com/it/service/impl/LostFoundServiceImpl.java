package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dao.LostFoundMapper;
import com.it.entity.LostFound;
import com.it.service.LostFoundService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Override
    public List<LostFound> legalList() {
        List<LostFound> lostFounds = this.list();
        //获取审核成功的lostfounds
        return lostFounds.stream().filter(lostFound -> lostFound.getReviewProcess() == 1).toList();
    }
}
