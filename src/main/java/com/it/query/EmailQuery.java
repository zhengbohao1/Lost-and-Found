package com.it.query;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ClassName: EmailQuery
 * Description:
 * 发送邮箱验证码的请求体实体类
 * @Author Joel
 * @Create 2024/9/20 19:16
 * @Version 1.0
 */

@Data
public class EmailQuery {

    @Email
    @NotEmpty
    private String email;

    @NotNull
    private Integer type;
}
