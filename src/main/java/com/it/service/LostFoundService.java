package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.LostFound;

import java.util.List;

public interface LostFoundService extends IService<LostFound> {
    List<LostFound>legalList();
}
