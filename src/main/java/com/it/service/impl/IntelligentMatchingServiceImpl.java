package com.it.service.impl;

import com.it.entity.LostFound;
import com.it.entity.MessageNotification;
import com.it.entity.MissingNotices;
import com.it.enums.MessageType;
import com.it.service.*;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntelligentMatchingServiceImpl implements IntelligentMatchingService {

    @Resource
    private AiManager aiManager;
    @Resource
    private IMissingNoticesService missingNoticesService;
    @Resource
    private ILostFoundService lostFoundService;
    @Resource
    private MessageNotificationService messageNotificationService;
    @Async
    @Override
    public void processSmartPush(LostFound lostFound) {
        List<MissingNotices> missingNoticesList = missingNoticesService.list();
        for (MissingNotices missingNotices : missingNoticesList) {
            String answer=aiManager.doSyncUnStableRequest("使用系统默认", "请问"+lostFound.getItemName() +
                    "和"+missingNotices.getItemName()+"的相似度为多少？" +
                    "请你可以根据现实意义分析" + "这俩是一个东西的概率，通过对两个物品名的分析，返回你认为的百分比。" +
                    "例如：‘oppo手机’与‘手机’有很高的概率是同一个物品，但是'oppo手机'与'华硕电脑'或者‘马克杯子’或者‘眼镜’的相关性明显为0。现在请你解答，" +
                    "告诉我具体的百分比，例如80%，注意！！！回答时只用回答具体的比例！！！不要输出多余的任何内容！！！哪怕你分析不了，也请返回0%！！！");
            // 定义正则表达式
            String regex = "content=([^,]+)";

            // 创建 Pattern 和 Matcher
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(answer);
            int contentPercentage=0;
            // 查找匹配项
            if (matcher.find()) {
                String contentValue = matcher.group(1).trim(); // 提取内容并去除空格
                // 去掉百分号并转换为数字
                try {
                    contentPercentage = Integer.parseInt(contentValue.replace("%", "").trim());
                } catch (NumberFormatException e) {
                    System.out.println("无法解析百分比");
                    continue;
                }
            } else {
                System.out.println("gpt问答出了问题");
                continue;
            }
            if (contentPercentage > 30) {
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
    }

    @Async
    @Override
    public void processSmartPush2(MissingNotices missingNotices) {
        List<LostFound> lostFoundsList = lostFoundService.list();
        for (LostFound lostFound : lostFoundsList) {
            String answer=aiManager.doSyncUnStableRequest("使用系统默认", "请问"+lostFound.getItemName() +
                    "和"+missingNotices.getItemName()+"的相似度为多少？" +
                    "请你可以根据现实意义分析" + "这俩是一个东西的概率，通过对两个物品名的分析，返回你认为的百分比。" +
                    "例如：‘oppo手机’与‘手机’有很高的概率是同一个物品，但是'oppo手机'与'华硕电脑'或者‘马克杯子’或者‘眼镜’的相关性明显为0。现在请你解答，" +
                    "告诉我具体的百分比，例如80%，注意！！！回答时只用回答具体的比例！！！不要输出多余的任何内容！！！哪怕你分析不了，也请返回0%！！！");
            // 定义正则表达式
            String regex = "content=([^,]+)";

            // 创建 Pattern 和 Matcher
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(answer);
            int contentPercentage=0;
            // 查找匹配项
            if (matcher.find()) {
                String contentValue = matcher.group(1).trim(); // 提取内容并去除空格
                // 去掉百分号并转换为数字
                try {
                    contentPercentage = Integer.parseInt(contentValue.replace("%", "").trim());
                } catch (NumberFormatException e) {
                    System.out.println("无法解析百分比" );
                    continue;
                }
            } else {
                System.out.println("gpt问答出了问题");
                continue;
            }
            if (contentPercentage > 60) {
                System.out.println(lostFound.getItemName()+"与"+missingNotices.getItemName()+"相似度："+contentPercentage);
                String ownerId = lostFound.getFinderId();
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
    }

}
