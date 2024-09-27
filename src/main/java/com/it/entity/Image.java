package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
@Data
@TableName("image")
public class Image {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Getter
    @Setter
    private int width;
    @Getter
    @Setter
    private int height;
    @Getter
    @Setter
    private String path;
}
