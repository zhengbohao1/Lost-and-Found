package com.it.controller;

import com.it.common.R;
import com.it.entity.UserInfo;
import com.it.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: AdminController
 * Description:
 * 管理员进行用户管理相关控制层
 * @Author Joel
 * @Create 2024/9/29 10:39
 * @Version 1.0
 */

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取所有用户详细信息
     * @return
     */
    @GetMapping("/userInfo/all")
    public R<List<UserInfo>> getUserInfo(){
        List<UserInfo> userInfoList = userInfoService.getAllUserInfo();
        return R.success(userInfoList);
    }

    /**
     * 禁用用户的账号
     * @param userId 要禁用的用户的id
     * @return
     */
    @GetMapping("/disable")
    public R<String> disableUser(String userId){
        userInfoService.disableUser(userId);
        return R.success("禁用成功");
    }

}