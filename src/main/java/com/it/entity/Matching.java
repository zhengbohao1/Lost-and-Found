package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Data
@TableName("matching")
public class Matching {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Getter
    @Setter
    private String userId;
    @Getter
    @Setter
    private int postId;
    @Getter
    @Setter
    private int category;
}
