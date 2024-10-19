package com.it.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.it.enums.MessageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@TableName("message_notification")
public class MessageNotification implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Integer id;
    // 消息的接收者ID
    @Getter
    @Setter
    private String recipientId;
    // 消息类型 (例如：审核不通过、评论回复、系统通知等)
    @Getter
    @Setter
    private MessageType messageType;
    // 消息的内容
    @Getter
    @Setter
    private String messageContent;
    // 消息状态 (是否已读)
    @Getter
    @Setter
    private int isRead;
    // 发送时间
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    // 额外的关联信息，例如帖子ID、评论ID
    @Getter
    @Setter
    private int relatedPostId; // 可以为空
    @Getter
    @Setter
    private int relatedCommentId; // 可以为空
    @Getter
    @Setter
    private String senderId;
    @Getter
    @Setter
    private int postCategory;
}
