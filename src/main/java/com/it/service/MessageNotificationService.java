package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Image;
import com.it.entity.MessageNotification;

import java.util.List;

public interface MessageNotificationService extends IService<MessageNotification> {
    void sendRejectedMessage(String recipientId,int relatedPostId,int postCategory);
    List<MessageNotification> getRejectedMessages(String userId);
    void sendApprovedMessage(String recipientId,int relatedPostId,int postCategory);
    List<MessageNotification> getApprovedMessages(String userId);
    List<MessageNotification> getConfirmMessages(String userId);
    List<MessageNotification> getGoldMessages(String userId);
    void sendLostParentReplyMessage(String senderId, int relatedPostId, String postWriterId,String content);
    //发布的评论收到了一个子评论！，通过父评论id获取发布者id，然后发布给他。
    void sendLostChildReplyMessage(String senderId, int parentId,int relatedPostId,String content);
    void sendMissingParentReplyMessage(String senderId, int relatedPostId, String postWriterId,String content);
    void sendMissingChildReplyMessage(String senderId, int parentId,int relatedPostId,String content);
    List<MessageNotification> getAllReplyMessages(String userId);
    //标记已读
    void markMessageAsRead(int id);
    int getUnreadCount(String userId);
}
