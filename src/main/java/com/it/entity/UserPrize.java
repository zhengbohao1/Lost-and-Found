package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_prize
 */
@TableName(value ="user_prize")
@Data
public class UserPrize implements Serializable {
    private String userId;

    private String prizeName;

    private Date exchangedTime;

    private static final long serialVersionUID = 1L;
}