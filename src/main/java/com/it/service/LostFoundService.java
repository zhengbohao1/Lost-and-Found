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
    String confirmClaim(int id, String user_id);
    LostFoundDto getBypostId(int id);
    List<LostFoundDto> getByUserId(String userId);
    List<LostFoundDto> getIllegalByUserId(String userId);
    List<LostFoundDto> getWaitByUserId(String userId);
    void UpdateLostfound(LostFound lostFound);
}
