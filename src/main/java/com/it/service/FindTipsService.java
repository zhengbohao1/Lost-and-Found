package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.FindTips;
import com.it.entity.Image;
import org.hibernate.annotations.processing.Find;

public interface FindTipsService extends IService<FindTips> {
    void my_save(FindTips findTips);
}
