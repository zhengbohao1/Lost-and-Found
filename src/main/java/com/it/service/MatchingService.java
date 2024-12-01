package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.common.R;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.Image;
import com.it.entity.LostFound;
import com.it.entity.Matching;
import com.it.entity.MissingNotices;

import java.util.List;

public interface MatchingService extends IService<Matching> {
    public R<List<MissingNoticesDto>> getMatchPosts(String userId);
    public R<List<LostFoundDto>> getLostMatchPosts(String userId);
    void my_save(Matching matching);
}
