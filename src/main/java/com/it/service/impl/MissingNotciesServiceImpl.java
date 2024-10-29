package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.*;
import com.it.enums.MessageType;
import com.it.exception.BusinessException;
import com.it.mapper.MissingNoticesMapper;
import com.it.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MissingNotciesServiceImpl extends ServiceImpl<MissingNoticesMapper, MissingNotices> implements MissingNoticesService {
    @Autowired
    private ImageServiceImpl imageService;
    @Value("${project.folder}")//读取配置文件里的值,写的是"${reggie.path}",它自动变成赋予的值了
    private String basepath;
    @Autowired
    private TradingVolumeService tradingVolumeService;
    @Resource
    private ILostFoundService iLostFoundService;
    @Resource
    private MessageNotificationService messageNotificationService;
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
        List<MissingNotices> missingNotices = baseMapper.selectList(queryWrapper).stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 1).toList();;
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
        String imagePath = basepath + "lost_found_img/" + missingNotices.getImgUrl();

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

                // 使用改进的智能推送逻辑
                List<LostFound> lostFoundList = iLostFoundService.list();

                for (LostFound lostFound : lostFoundList) {
                    if (isSimilar(missingNotices.getItemName(), lostFound.getItemName())) {
                        String ownerId = missingNotices.getOwnerId();
                        MessageNotification messageNotification = new MessageNotification();
                        messageNotification.setRecipientId(ownerId);
                        messageNotification.setMessageType(MessageType.SYSTEM_NOTIFICATION);
                        messageNotification.setMessageContent("您有一条智能匹配信息推送！请点击详情查看！");
                        messageNotification.setRelatedPostId(lostFound.getId());
                        messageNotification.setIsRead(0);
                        messageNotification.setSenderId("系统");
                        messageNotification.setPostCategory(0);
                        messageNotificationService.save(messageNotification);
                    }
                }
            } else {
                throw new BusinessException("图片不存在");
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
    }


    @Override
    public MissingNoticesDto getBypostId(int id) {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        MissingNotices missingNotice = this.getOne(queryWrapper);
        String path = missingNotice.getImgUrl();
        QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("path", path);
        Image image = imageService.getOne(queryWrapper2);
        MissingNoticesDto dto = new MissingNoticesDto(); // 创建一个新的 LostFoundDto 对象
       BeanUtils.copyProperties(missingNotice, dto); //
        if (image != null) {
            dto.setWidth(image.getWidth());  // 设置图片宽度
            dto.setHeight(image.getHeight()); // 设置图片高度
        }
        return dto;
    }

    @Override
    public List<MissingNoticesDto> getWaitByUserId(String userId) {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", userId);
        List<MissingNotices> missingNotices =this.baseMapper.selectList(queryWrapper).stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 0).toList();
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

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
    public List<MissingNoticesDto> getByUserId(String userId) {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", userId);
        List<MissingNotices> missingNotices =this.baseMapper.selectList(queryWrapper).stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 1).toList();
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

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
    public List<MissingNoticesDto> getIllegalByUserId(String userId) {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", userId);
        List<MissingNotices> missingNotices =this.baseMapper.selectList(queryWrapper).stream().filter(MissingNotices -> MissingNotices.getReviewProcess() == 2).toList();
        List<MissingNoticesDto> missingNoticesDtos = new ArrayList<>();
        //
        for (MissingNotices missingnotice : missingNotices) {
            String path = missingnotice.getImgUrl();
            QueryWrapper<Image> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("path", path);
            Image image = imageService.getOne(queryWrapper2);

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
    public String confirmFindTips(int post_id, String user_id) {
        try {
            this.baseMapper.confirmMissingNotices(post_id, user_id);
            Date currentDate = Date.valueOf(LocalDate.now());
            //先判断有没有今天的已经生成了的记录
            QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("date", currentDate)
                    .eq("category", "寻物启事");
            if (tradingVolumeService.getOne(queryWrapper) != null) {
                //如果有，那么就直接加1
                TradingVolume one = tradingVolumeService.getOne(queryWrapper);
                one.setNum(one.getNum() + 1);
                tradingVolumeService.updateById(one);
            }
            else{
                //如果没有，那么就新建一个，并且设置数量为1
                TradingVolume tradingVolume = new TradingVolume();
                tradingVolume.setDate(currentDate);
                tradingVolume.setNum(1);
                tradingVolume.setCategory("寻物启事");
                tradingVolumeService.save(tradingVolume);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return "寻物信息已确认";
    }

    @Override
    public Long get_count() {
        return this.baseMapper.selectCount(null);
    }
    @Override
    public Long get_found_count() {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("finder_id");
        return this.baseMapper.selectCount(queryWrapper);
    }
    // 新增 isSimilar 方法，使用 Jaccard 相似度算法
    private boolean isSimilar(String text1, String text2) {
        // 定义停用词列表，中文可加“的”、“了”等
        List<String> stopWords = List.of("的", "是", "了", "我", "你", "他", "我们");

        // 分词并去掉停用词
        Set<String> words1 = Arrays.stream(text1.split("\\s+"))
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toSet());
        Set<String> words2 = Arrays.stream(text2.split("\\s+"))
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toSet());

        // 若存在子字符串关系，直接判定为相似
        if (isSubstringMatch(words1, words2)) {
            return true;
        }

        // 使用 Jaccard 相似度算法计算相似度
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2); // 交集

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2); // 并集

        double similarity = (double) intersection.size() / union.size();
        System.out.println(similarity);
        return similarity >= 0.3; // 相似度阈值
    }
    // 辅助方法：检测子字符串匹配
    private boolean isSubstringMatch(Set<String> words1, Set<String> words2) {
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.contains(word2) || word2.contains(word1)) {
                    return true;
                }
            }
        }
        return false;
    }
}
