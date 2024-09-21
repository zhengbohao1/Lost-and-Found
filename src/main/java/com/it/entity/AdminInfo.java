package com.it.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName admin_info
 */
@TableName(value ="admin_info")
@Data
public class AdminInfo implements Serializable {

    @TableId
    private Integer adminId;

    private String email;

    private String nickName;

    private String password;

    private static final long serialVersionUID = 1L;
}