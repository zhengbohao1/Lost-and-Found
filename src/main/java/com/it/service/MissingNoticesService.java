package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;

import java.util.List;

public interface MissingNoticesService extends IService<MissingNotices> {
    List<MissingNotices> legalList();
}
