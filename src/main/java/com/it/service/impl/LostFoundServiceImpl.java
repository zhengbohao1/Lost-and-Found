package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.LostFoundDto;
import com.it.entity.Advises;
import com.it.entity.Image;
import com.it.exception.BusinessException;
import com.it.mapper.LostFoundMapper;
import com.it.entity.LostFound;
import com.it.service.ImageService;
import com.it.service.LostFoundService;
import com.it.service.MessageNotificationService;
import jakarta.annotation.Resource;
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
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Autowired
    ImageService imageService;


    @Value("${project.folder}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @Override
    public List<LostFoundDto> legalList() {
        List<LostFound> lostFounds = this.list().stream().filter(lostFound -> lostFound.getReviewProcess() == 1).toList();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();


        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper, false);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }


    @Override
    public List<LostFoundDto> selectByCondition(String content) {
        QueryWrapper<LostFound> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.like("item_name",content);
        List<LostFound> lostFounds= baseMapper.selectList(queryWrapper1);
        //
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();

        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }

    @Override
    public List<LostFoundDto> get_list() {
        List<LostFound> lostFounds = this.list();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();

        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            queryWrapper.eq("path", path);
            Image image = imageService.getOne(queryWrapper);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }

    @Override
    public void my_save(LostFound lostFound) {
        save(lostFound);
        String imagePath  = basepath+"lost_found_img/"+lostFound.getImgUrl();
        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                // 获取图片的宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();
                Image img = new Image();
                img.setPath(lostFound.getImgUrl());
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
    public String confirmClaim(int id, String user_id) {
        try {
            this.baseMapper.confirmLost(id, user_id);
            return "认领信息已确认";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public LostFoundDto getBypostId(int id) {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        LostFound lostFound = this.getOne(queryWrapper);
        String path = lostFound.getImgUrl();
        QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("path", path);
        Image image = imageService.getOne(queryWrapper2);
        LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
        BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
        if (image != null) {
            dto.setWidth(image.getWidth());  // 设置图片宽度
            dto.setHeight(image.getHeight()); // 设置图片高度
        }
        return dto;
    }

    @Override
    public List<LostFoundDto> getByUserId(String userId) {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("finder_id", userId);
        List<LostFound> lostFounds =this.baseMapper.selectList(queryWrapper).stream().filter(lostFound -> lostFound.getReviewProcess() == 1).toList();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();

        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }

    @Override
    public List<LostFoundDto> getIllegalByUserId(String userId) {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("finder_id", userId);
        List<LostFound> lostFounds =this.baseMapper.selectList(queryWrapper).stream().filter(lostFound -> lostFound.getReviewProcess() == 2).toList();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();

        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }

    @Override
    public List<LostFoundDto> getWaitByUserId(String userId) {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("finder_id", userId);
        List<LostFound> lostFounds =this.baseMapper.selectList(queryWrapper).stream().filter(lostFound -> lostFound.getReviewProcess() == 0).toList();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();

        for (LostFound lostFound : lostFounds) {
            String path = lostFound.getImgUrl();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

            LostFoundDto dto = new LostFoundDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(lostFound, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }

            lostFoundsDto.add(dto); // 将 dto 添加到列表中
        }

        return lostFoundsDto;
    }

    @Override
    public void UpdateLostfound(LostFound lostFound) {
        this.baseMapper.updateById(lostFound);
    }
}
