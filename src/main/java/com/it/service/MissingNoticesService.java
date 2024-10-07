package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;

import java.util.List;

public interface MissingNoticesService extends IService<MissingNotices> {
    List<MissingNoticesDto> legalList();
    List<MissingNoticesDto> selectByCondition(String content);
    List<MissingNoticesDto> get_list();
    void my_save(MissingNotices missingNotices);
}
