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
@TableName("advises")
public class Advises {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Setter
    @Getter
    private String userId;
    @Setter
    @Getter
    private String category;
    @Setter
    @Getter
    private String content;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
