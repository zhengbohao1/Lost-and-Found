package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.Image;
import com.it.entity.LostFound;
import com.it.entity.Matching;
import com.it.entity.MissingNotices;
import com.it.mapper.ImageMapper;
import com.it.mapper.MatchingMapper;
import com.it.service.ILostFoundService;
import com.it.service.IMissingNoticesService;
import com.it.service.ImageService;
import com.it.service.MatchingService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingServiceImpl extends ServiceImpl<MatchingMapper, Matching> implements MatchingService {
    @Resource
    private ILostFoundService lostFoundService;
    @Resource
    private IMissingNoticesService missingNotices;
    @Resource
    private ImageService imageService;

    @Override
    public R<List<MissingNoticesDto>> getMatchPosts(String userId){
        QueryWrapper<Matching> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Matching> matchingList = list(queryWrapper).stream().filter(Matching -> Matching.getCategory()==1).toList();
        List<MissingNoticesDto> missingNoticesListdto=new ArrayList<>();
        for (Matching matching : matchingList) {
            int postId = matching.getPostId();
            MissingNotices missingNotice = missingNotices.getById(postId);
            //
            String path = missingNotice.getImgUrl();
            QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

            MissingNoticesDto dto = new MissingNoticesDto(); // 创建一个新的 LostFoundDto 对象
            BeanUtils.copyProperties(missingNotice, dto); // 将 lostFound 的属性复制到 dto
            if (image != null) {
                dto.setWidth(image.getWidth());  // 设置图片宽度
                dto.setHeight(image.getHeight()); // 设置图片高度
            }
            missingNoticesListdto.add(dto); // 将 dto 添加到列表中
        }
        return R.success(missingNoticesListdto);
    }
    @Override
    public R<List<LostFoundDto>> getLostMatchPosts(String userId){
        QueryWrapper<Matching> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Matching> matchingList = list(queryWrapper).stream().filter(Matching -> Matching.getCategory()==0).toList();
        List<LostFoundDto> lostFoundsDto = new ArrayList<>();
        for (Matching matching : matchingList) {
            int postId = matching.getPostId();
            LostFound lostFound = lostFoundService.getById(postId);
            //
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
            //
            lostFoundsDto.add(dto);
        }
        return R.success(lostFoundsDto);
    }

    @Override
    public void my_save(Matching matching) {
        String userId = matching.getUserId();
        int postId = matching.getPostId();
        int category = matching.getCategory();
        QueryWrapper<Matching> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("post_id", postId)
                .eq("category", category);
        boolean exists = exists(queryWrapper);
        if (!exists) {
            save(matching);
        }
    }
}
