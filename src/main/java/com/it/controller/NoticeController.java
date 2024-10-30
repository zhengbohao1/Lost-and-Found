package com.it.controller;

import com.it.common.R;
import com.it.dto.FindTipsDto;
import com.it.entity.ClaimRequest;
import com.it.entity.MessageNotification;
import com.it.exception.BusinessException;
import com.it.service.ClaimRequestService;
import com.it.service.FindTipsService;
import com.it.service.MessageNotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/notice")
public class NoticeController {
    @Resource
    private ClaimRequestService claimRequestService;
    @Resource
    private FindTipsService findTipsService;
    @Resource
    private MessageNotificationService messageNotificationService;
    @PutMapping(value = "/markClaimAsRead")
    public R<String> markClaimAsRead(int id){
        try {
            claimRequestService.MarkClaimAsRead(id);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success("已标记为已读");
    }
    /**
     * 获取收到的认领消息
     * @return
     */
    @GetMapping("/getClaimMessage")
    public R<List<ClaimRequest>> getClaimMessage(){
        List<ClaimRequest> claimMessage = claimRequestService.getClaimMessage();
        return R.success(claimMessage);
    }
    @GetMapping("/getFindTips")
    public R<List<FindTipsDto>> getFindTips(String ownerId){
        List<FindTipsDto> findTips = findTipsService.get_findTips(ownerId);
        return R.success(findTips);
    }
    @GetMapping("/getUnreadCount")
    public R<Integer> getUnreadCount(String userId){
        int unreadCount = claimRequestService.getUnreadCount(userId);
        int unreadCount2 = messageNotificationService.getUnreadCount(userId);
        int unreadCount3 = findTipsService.getUnreadCount(userId);
        return R.success(unreadCount+unreadCount2+unreadCount3);
    }
    @GetMapping("/getRejectedMessages")
    public R<List<MessageNotification>> getRejectedMessages(String userId){
        List<MessageNotification> rejectedMessages = messageNotificationService.getRejectedMessages(userId);
        return R.success(rejectedMessages);
    }
    @GetMapping("/getApprovedMessages")
    public R<List<MessageNotification>> getApprovedMessages(String userId){
        List<MessageNotification> approvedMessages = messageNotificationService.getApprovedMessages(userId);
        return R.success(approvedMessages);
    }
    @GetMapping("/getAllReplyMessages")
    public R<List<MessageNotification>> getAllReplyMessages(String userId){
        List<MessageNotification> allReplyMessages = messageNotificationService.getAllReplyMessages(userId);
        return R.success(allReplyMessages);
    }
    @PutMapping("/markMessageAsRead")
    public R<String> markMessageAsRead(int id){
        messageNotificationService.markMessageAsRead(id);
        return R.success("已标记为已读");
    }
    @PutMapping("/markFindTipsAsRead")
    public R<String> markFindTipsAsRead(int id){
        findTipsService.markFindTipsAsRead(id);
        return R.success("已标记为已读");
    }
    @GetMapping("/getAllConfirmMessages")
    public R<List<MessageNotification>> getAllConfirmMessages(String userId){
        List<MessageNotification> allConfirmMessages = messageNotificationService.getConfirmMessages(userId);
        return R.success(allConfirmMessages);
    }
    @GetMapping("/getGoldMessages")
    public R<List<MessageNotification>> getGoldMessages(String userId){
        List<MessageNotification> goldMessages = messageNotificationService.getGoldMessages(userId);
        return R.success(goldMessages);
    }
}
