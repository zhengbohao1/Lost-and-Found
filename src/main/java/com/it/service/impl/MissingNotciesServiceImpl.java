package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dao.LostFoundMapper;
import com.it.dao.MissingNoticesMapper;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import com.it.service.LostFoundService;
import com.it.service.MissingNoticesService;
import org.springframework.stereotype.Service;

@Service
public class MissingNotciesServiceImpl extends ServiceImpl<MissingNoticesMapper, MissingNotices> implements MissingNoticesService {
}
