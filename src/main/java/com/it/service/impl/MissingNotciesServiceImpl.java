package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.Image;
import com.it.entity.LostFound;
import com.it.exception.BusinessException;
import com.it.mapper.MissingNoticesMapper;
import com.it.entity.MissingNotices;
import com.it.service.MissingNoticesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MissingNotciesServiceImpl extends ServiceImpl<MissingNoticesMapper, MissingNotices> implements MissingNoticesService {
    @Autowired
    private ImageServiceImpl imageService;
    @Value("${project.folder}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @Override
    public List<MissingNoticesDto> legalList() {
        List<MissingNotices> missingNotices = list().stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 1).toList();
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper);

            MissingNoticesDto dto = new MissingNoticesDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(missingnotice, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            missingNoticesDtos.add(dto); // 将 dto 添加到列表中
        }
        return missingNoticesDtos;

    }

    @Override
    public List<MissingNoticesDto> selectByCondition(String content) {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("item_name",content);
        List<MissingNotices> missingNotices = baseMapper.selectList(queryWrapper);
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper1 = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper1);

            MissingNoticesDto dto = new MissingNoticesDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(missingnotice, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            missingNoticesDtos.add(dto); // 将 dto 添加到列表中
        }
        return missingNoticesDtos;
    }

    @Override
    public List<MissingNoticesDto> get_list() {
        List<MissingNotices> missingNotices = list();
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper);

            MissingNoticesDto dto = new MissingNoticesDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(missingnotice, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            missingNoticesDtos.add(dto); // 将 dto 添加到列表中
        }
        return missingNoticesDtos;
    }

    @Override
    public void my_save(MissingNotices missingNotices) {
        save(missingNotices);
        String imagePath  = basepath+"lost_found_img/"+missingNotices.getImgUrl();
        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                // 获取图片的宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();
                Image img = new Image();
                img.setPath(missingNotices.getImgUrl());
                img.setWidth(width);
                img.setHeight(height);
                imageService.save(img);
            } else {
                throw  new BusinessException("图片不存在");
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
