package com.it.service;

import com.it.common.R;
import com.it.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    //根据昵称获取用户信息
    R<UserInfo> getUserInfo(String nickName);

    //修改密码
    void updatePwd(String oldPwd, String newPwd, String rePwd);

    //忘记密码时的重置密码
    void resetPwd(String email, String password, String emailCode);

    //获取所有用户信息
    List<UserInfo> getAllUserInfo();

    //禁用用户的账号
    void disableUser(String userId);
    //根据id获取用户名
    String getUserNameById(String userId);
}
