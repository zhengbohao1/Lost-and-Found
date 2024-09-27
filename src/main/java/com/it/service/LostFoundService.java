package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.dto.LostFoundDto;
import com.it.entity.Advises;
import com.it.entity.LostFound;

import java.util.List;

public interface LostFoundService extends IService<LostFound> {
    List<LostFoundDto> legalList();
    List<LostFoundDto> selectByCondition(String content);
    List<LostFoundDto> get_list();
    void my_save(LostFound lostFound);
}
