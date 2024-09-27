package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Image;
import com.it.entity.MissingNotices;
import com.it.mapper.ImageMapper;
import com.it.mapper.MissingNoticesMapper;
import com.it.service.ImageService;
import com.it.service.MissingNoticesService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
}
