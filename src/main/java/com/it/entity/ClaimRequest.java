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

@Data
@TableName("claim_request")
public class ClaimRequest {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Setter
    @Getter
    @TableField(fill = FieldFill.INSERT)
    private String userId;
    @Setter
    @Getter
    private String userName;
    @Setter
    @Getter
    private String contactWay;
    @Setter
    @Getter
    private String contactDetails;
    @Setter
    @Getter
    private String studentId;
    @Setter
    @Getter
    private String evidence;
    @Setter
    @Getter
    private String notes;
    @Setter
    @Getter
    private String postId;
    @Setter
    @Getter
    private String finderId;
}
