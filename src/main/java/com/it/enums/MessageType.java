package com.it.enums;

public enum MessageType {
    POST_REJECTED,    // 帖子审核不通过
    POST_APPROVED,    // 帖子审核通过
    COMMENT_REPLY,    // 收到评论回复
    SYSTEM_NOTIFICATION,  // 系统通知, 例如：物品的智能推送提醒
}
