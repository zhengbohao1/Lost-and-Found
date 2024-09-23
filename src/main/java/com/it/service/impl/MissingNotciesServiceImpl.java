package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.mapper.MissingNoticesMapper;
import com.it.entity.MissingNotices;
import com.it.service.MissingNoticesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissingNotciesServiceImpl extends ServiceImpl<MissingNoticesMapper, MissingNotices> implements MissingNoticesService {
    @Override
    public List<MissingNotices> legalList() {
        List<MissingNotices> missingNotices = list();
        //获取申通通过的招领信息
        return missingNotices.stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 1).toList();
    }
}
