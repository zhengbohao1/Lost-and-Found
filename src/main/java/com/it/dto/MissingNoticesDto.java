package com.it.dto;

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
public class MissingNoticesDto implements Serializable {

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
    private LocalDateTime lostDate;
    @Getter
    @Setter
    private String lostLocation;
    @Getter
    @Setter
    private int ownerId;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @Getter
    @Setter
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @Getter
    @Setter
    private String imgUrl;
    @Getter
    @Setter
    private int reviewProcess;
    @Getter
    @Setter
    private int width;
    @Getter
    @Setter
    private int height;
}
