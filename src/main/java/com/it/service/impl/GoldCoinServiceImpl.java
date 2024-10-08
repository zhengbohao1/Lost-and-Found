package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.GoldCoin;
import com.it.entity.Image;
import com.it.mapper.GoldCoinMapper;
import com.it.mapper.ImageMapper;
import com.it.service.GoldCoinService;
import com.it.service.ImageService;
import org.springframework.stereotype.Service;

@Service
public class GoldCoinServiceImpl extends ServiceImpl<GoldCoinMapper, GoldCoin> implements GoldCoinService {
}
