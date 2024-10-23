package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.FindTipsDto;
import com.it.dto.LostFoundDto;
import com.it.entity.FindTips;
import com.it.entity.Image;
import com.it.entity.LostFound;
import com.it.exception.BusinessException;
import com.it.mapper.FindTipsMapper;
import com.it.mapper.ImageMapper;
import com.it.service.FindTipsService;
import com.it.service.ImageService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FindTipsServiceImpl extends ServiceImpl<FindTipsMapper, FindTips> implements FindTipsService {
    @Value("${project.folder}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @Resource
    private ImageService imageService;
    @Override
    public void my_save(FindTips findTips) {
        save(findTips);
        String imagePath  = basepath+"find_tip_img/"+findTips.getImgUrl();
        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                // 获取图片的宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();
                Image img = new Image();
                img.setPath(findTips.getImgUrl());
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

    @Override
    public List<FindTipsDto> get_findTips(String ownerId) {
        QueryWrapper<FindTips> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id",ownerId);
        List<FindTips> findTips = this.baseMapper.selectList(queryWrapper);
        List<FindTipsDto> findTipsDtos = new ArrayList<>();
        //开始填充
        for (FindTips findtips : findTips) {
            String path = findtips.getImgUrl();
            QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper2, false);

            FindTipsDto dto = new FindTipsDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(findtips, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            findTipsDtos.add(dto); // 将 dto 添加到列表中
        }
        return findTipsDtos;
    }

    @Override
    public void markFindTipsAsRead(int id) {
        FindTips tip = getById(id);
        tip.setIsRead(1);
        updateById(tip);
    }
}
