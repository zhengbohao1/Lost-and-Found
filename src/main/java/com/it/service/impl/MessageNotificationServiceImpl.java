package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.ClaimRequest;
import com.it.entity.Comments;
import com.it.entity.Image;
import com.it.entity.MessageNotification;
import com.it.enums.MessageType;
import com.it.mapper.ImageMapper;
import com.it.mapper.MessageMapper;
import com.it.service.CommentsService;
import com.it.service.ImageService;
import com.it.service.MessageNotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageNotificationServiceImpl extends ServiceImpl<MessageMapper, MessageNotification> implements MessageNotificationService {

    @Resource
    private CommentsService commentsService;
    @Override
    public void sendRejectedMessage(String recipientId, int relatedPostId,int postCategory) {
        MessageNotification notification = new MessageNotification();
        notification.setRecipientId(recipientId);
        notification.setMessageType(MessageType.POST_REJECTED);
        notification.setMessageContent("您发布的帖子审核未通过，请在个人主页修改帖子重新发布申请。");
        notification.setPostCategory(postCategory);
        notification.setRelatedPostId(relatedPostId);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public List<MessageNotification> getRejectedMessages(String userId) {
        QueryWrapper<MessageNotification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recipient_id", userId)
                .eq("message_type", MessageType.POST_REJECTED);
        return list(queryWrapper);
    }

    @Override
    public void sendApprovedMessage(String recipientId, int relatedPostId,int postCategory) {
        MessageNotification notification = new MessageNotification();
        notification.setRecipientId(recipientId);
        notification.setMessageType(MessageType.POST_APPROVED);
        notification.setMessageContent("您发布的帖子审核通过，您可以在个人主页查看帖子详情。");
        notification.setRelatedPostId(relatedPostId);
        notification.setPostCategory(postCategory);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public List<MessageNotification> getApprovedMessages(String userId) {
        QueryWrapper<MessageNotification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recipient_id", userId)
                .eq("message_type", MessageType.POST_APPROVED);
        return list(queryWrapper);
    }

    @Override
    public void sendLostParentReplyMessage(String senderId, int relatedPostId, String postWriterId, String content) {
        MessageNotification notification = new MessageNotification();
        notification.setRecipientId(postWriterId);
        notification.setMessageType(MessageType.COMMENT_REPLY);
        notification.setMessageContent(content);
        notification.setSenderId(senderId);
        notification.setRelatedPostId(relatedPostId);
        notification.setPostCategory(0);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public void sendLostChildReplyMessage( String senderId, int parentId,int relatedPostId, String content) {
        MessageNotification notification = new MessageNotification();
        Comments comments = commentsService.getById(parentId);
        String recipientId = comments.getUserId();//接受者id，即父评论人的id！！
        notification.setRecipientId(recipientId);
        notification.setMessageType(MessageType.COMMENT_REPLY);
        notification.setMessageContent(content);
        notification.setSenderId(senderId);
        notification.setRelatedPostId(relatedPostId);
        notification.setPostCategory(0);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public void sendMissingParentReplyMessage(String senderId, int relatedPostId, String postWriterId, String content) {
        MessageNotification notification = new MessageNotification();
        notification.setRecipientId(postWriterId);
        notification.setMessageType(MessageType.COMMENT_REPLY);
        notification.setMessageContent(content);
        notification.setSenderId(senderId);
        notification.setRelatedPostId(relatedPostId);
        notification.setPostCategory(1);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public void sendMissingChildReplyMessage(String senderId, int parentId, int relatedPostId, String content) {
        MessageNotification notification = new MessageNotification();
        Comments comments = commentsService.getById(parentId);
        String recipientId = comments.getUserId();//接受者id，即父评论人的id！！
        notification.setRecipientId(recipientId);
        notification.setMessageType(MessageType.COMMENT_REPLY);
        notification.setMessageContent(content);
        notification.setSenderId(senderId);
        notification.setRelatedPostId(relatedPostId);
        notification.setPostCategory(1);
        notification.setIsRead(0);
        save(notification);
    }

    @Override
    public List<MessageNotification> getAllReplyMessages(String userId) {
        QueryWrapper<MessageNotification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recipient_id", userId)
                .eq("message_type", MessageType.COMMENT_REPLY);
        return list(queryWrapper);
    }

    @Override
    public void markMessageAsRead(int id) {
        MessageNotification notification = getById(id);
        notification.setIsRead(1);
        this.updateById(notification);
    }
}
