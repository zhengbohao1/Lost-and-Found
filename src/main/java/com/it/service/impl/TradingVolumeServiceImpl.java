package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Image;
import com.it.entity.TradingVolume;
import com.it.mapper.ImageMapper;
import com.it.mapper.TradingVolumeMapper;
import com.it.service.ImageService;
import com.it.service.TradingVolumeService;
import org.springframework.stereotype.Service;

@Service
public class TradingVolumeServiceImpl extends ServiceImpl<TradingVolumeMapper, TradingVolume> implements TradingVolumeService {
}
