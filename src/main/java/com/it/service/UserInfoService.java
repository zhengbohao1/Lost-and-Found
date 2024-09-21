package com.it.service;

import com.it.common.R;
import com.it.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Yu
* @description 针对表【user_info】的数据库操作Service
* @createDate 2024-09-19 19:34:19
*/
public interface UserInfoService extends IService<UserInfo> {

    //用户注册
    void register(String email, String nickName, String password, String emailCode);

    //普通用户登录
    R<String> login(String email, String password);

}
