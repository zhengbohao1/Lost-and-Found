package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.AdminInfo;
import com.it.service.AdminInfoService;
import com.it.mapper.AdminInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Yu
* @description 针对表【admin_info】的数据库操作Service实现
* @createDate 2024-09-19 19:34:19
*/
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo>
    implements AdminInfoService{

}




