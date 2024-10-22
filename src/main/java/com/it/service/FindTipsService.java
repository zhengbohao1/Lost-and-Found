package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.dto.FindTipsDto;
import com.it.entity.FindTips;
import com.it.entity.Image;
import org.hibernate.annotations.processing.Find;

import java.util.List;

public interface FindTipsService extends IService<FindTips> {
    void my_save(FindTips findTips);
    List<FindTipsDto> get_findTips(String ownerId);
}
