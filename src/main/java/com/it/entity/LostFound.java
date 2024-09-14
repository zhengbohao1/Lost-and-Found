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
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("lost_found")
public class LostFound implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Getter
    @Setter
    private String itemName;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private LocalDateTime foundDate;
    @Getter
    @Setter
    private String foundLocation;
    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    private int finderId;
    @Getter
    @Setter
    private int claimantId;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
