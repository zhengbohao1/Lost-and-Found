package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dao.AdvisesMapper;
import com.it.entity.Advises;
import com.it.service.AdivisesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdivisesServiceImpl extends ServiceImpl<AdvisesMapper, Advises> implements AdivisesService {
    @Override
    public List<Advises> selectByCondition(String content) {
        QueryWrapper<Advises> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("content",content);
        return baseMapper.selectList(queryWrapper);
    }
}
