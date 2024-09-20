package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Advises;

import java.util.List;

public interface AdivisesService extends IService<Advises> {
    List<Advises> selectByCondition(String content);
}
