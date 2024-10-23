package com.it.service;

import com.it.common.R;
import com.it.entity.AdminInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Yu
* @description 针对表【admin_info】的数据库操作Service
* @createDate 2024-09-19 19:34:19
*/
public interface AdminInfoService extends IService<AdminInfo> {

    // 管理员登录
    R<String> adminLogin(String email, String password);
    int get_todayMissing_trade();
    int get_todayLost_trade();
    Long get_todayLost_post();
    Long get_todayMissing_post();
}
