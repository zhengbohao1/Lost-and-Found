package com.it.query;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * ClassName: PasswordQuery
 * Description:
 * 更新密码的请求体实体类
 * @Author Joel
 * @Create 2024/9/27 10:24
 * @Version 1.0
 */

@Data
public class PasswordQuery {

    @NotEmpty
    String oldPwd;

    @NotEmpty
    String newPwd;

    @NotEmpty
    String rePwd;
}
