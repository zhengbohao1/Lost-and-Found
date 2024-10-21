package com.it.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@TableName("find_tips")
public class FindTips {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)
    private String userId;
    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)
    private String userName;
    @Setter
    @Getter
    private String contactWay;
    @Setter
    @Getter
    private String contactDetails;
    @Setter
    @Getter
    private String content;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @Getter
    @Setter
    private int postId;
    @Getter
    @Setter
    private String ownerId;
    @Getter
    @Setter
    private String isRead;
    @Getter
    @Setter
    private String imgUrl;
}
