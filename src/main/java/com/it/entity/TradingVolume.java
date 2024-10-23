package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Date;

@Data
@TableName("trading_volume")
public class TradingVolume {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Getter
    @Setter
    private Date date;
    @Getter
    @Setter
    private Integer num;
    @Getter
    @Setter
    private String category;
}
