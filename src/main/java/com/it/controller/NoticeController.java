package com.it.controller;

import com.it.common.R;
import com.it.entity.ClaimRequest;
import com.it.exception.BusinessException;
import com.it.service.ClaimRequestService;
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
    @PutMapping(value = "/markAsRead")
    public R<String> markAsRead(int id){
        try {
            claimRequestService.MarkAsRead(id);
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
    @GetMapping("/getUnreadCount")
    public R<Integer> getUnreadCount(int userId){
        int unreadCount = claimRequestService.getUnreadCount(userId);
        return R.success(unreadCount);
    }
}
