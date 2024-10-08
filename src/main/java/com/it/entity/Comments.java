package com.it.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comments {
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
    private int isParent;
    @Setter
    @Getter
    private int parentId;
    @Setter
    @Getter
    private String comment;
    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
