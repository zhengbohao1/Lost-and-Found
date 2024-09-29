package com.it.query;

import com.it.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * ClassName: UserQuery
 * Description:
 * 用户登录相关请求的请求体实体类
 * @Author Joel
 * @Create 2024/9/20
 * @Version 1.0
 */

@Data
public class UserQuery {

    @Email
    @NotEmpty
    private String email;

    private String nickName;

    @NotEmpty
    @Password
    private String password;

    private String checkCode;

    private String emailCode;

}
