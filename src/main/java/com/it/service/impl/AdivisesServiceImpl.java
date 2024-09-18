package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dao.AdvisesMapper;
import com.it.entity.Advises;
import com.it.service.AdivisesService;
import org.springframework.stereotype.Service;

@Service
public class AdivisesServiceImpl extends ServiceImpl<AdvisesMapper, Advises> implements AdivisesService {
}
