package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName prize
 */
@TableName(value ="prize")
@Data
public class Prize implements Serializable {
    private Integer id;

    private String prizeName;

    private Integer prizePrice;

    private Integer prizeQuantity;

    private String prizeImageUrl;

    private static final long serialVersionUID = 1L;
}