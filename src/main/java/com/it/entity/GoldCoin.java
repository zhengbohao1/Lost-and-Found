package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Data
@TableName("gold_coin")
public class GoldCoin {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Getter
    @Setter
    private String userId;
    @Getter
    @Setter
    private Integer amount;
    @Getter
    @Setter
    private String email;
}
