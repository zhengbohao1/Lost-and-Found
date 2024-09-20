package com.it.service;

import com.it.common.R;
import com.it.entity.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Yu
* @description 针对表【email_code】的数据库操作Service
* @createDate 2024-09-19 19:34:19
*/
public interface EmailCodeService extends IService<EmailCode> {

    // 发送邮箱验证码
    void sendEmailCode(String email, Integer type);

    // 校验邮箱验证码
    void checkEmailCode(String email, String emailCode);

}
