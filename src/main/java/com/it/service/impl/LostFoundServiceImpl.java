package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.dto.LostFoundDto;
import com.it.entity.*;
import com.it.enums.MessageType;
import com.it.exception.BusinessException;
import com.it.mapper.LostFoundMapper;
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
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private TradingVolumeService tradingVolumeService;
    @Autowired
    private IMissingNoticesService missingNoticesService;
    @Autowired
    private MessageNotificationService messageNotificationService;

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
        String imagePath = basepath + "lost_found_img/" + lostFound.getImgUrl();

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

                // 使用改进的智能推送逻辑
                QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
                List<MissingNotices> missingNoticesList = missingNoticesService.list(queryWrapper);

                for (MissingNotices missingNotices : missingNoticesList) {
                    if (isSimilar(lostFound.getItemName(), missingNotices.getItemName())) {
                        String ownerId = missingNotices.getOwnerId();
                        MessageNotification messageNotification = new MessageNotification();
                        messageNotification.setRecipientId(ownerId);
                        messageNotification.setMessageType(MessageType.SYSTEM_NOTIFICATION);
                        messageNotification.setMessageContent("您有一条智能匹配信息推送！请点击详情查看！");
                        messageNotification.setRelatedPostId(missingNotices.getId());
                        messageNotification.setIsRead(0);
                        messageNotification.setSenderId("系统");
                        messageNotification.setPostCategory(1);
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
    public String confirmClaim(int id, String user_id) {
        try {
            this.baseMapper.confirmLost(id, user_id);
            Date currentDate = Date.valueOf(LocalDate.now());
            //先判断有没有今天的已经生成了的记录
            QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("date", currentDate)
                    .eq("category", "失物招领");
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
                tradingVolume.setCategory("失物招领");
                tradingVolumeService.save(tradingVolume);
            }
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

    @Override
    public Long get_count() {
        return this.baseMapper.selectCount(null);
    }

    @Override
    public Long get_found_count() {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
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
