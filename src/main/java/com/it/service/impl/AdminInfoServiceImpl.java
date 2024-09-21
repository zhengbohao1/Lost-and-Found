package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.entity.AdminInfo;
import com.it.exception.BusinessException;
import com.it.service.AdminInfoService;
import com.it.mapper.AdminInfoMapper;
import com.it.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author Yu
* @description 针对表【admin_info】的数据库操作Service实现
* @createDate 2024-09-19 19:34:19
*/

@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo>
    implements AdminInfoService{

    @Override
    public R<String> adminLogin(String email, String password) {
        // TODO: 校验管理员是否存在、密码是否正确
        AdminInfo adminInfo = this.baseMapper.selectByEmail(email);
        if(adminInfo == null || !adminInfo.getPassword().equals(password)){
            throw new BusinessException("邮箱或密码错误");
        }
        // TODO: 将管理员信息存储到token令牌中
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId",adminInfo.getAdminId());
        claims.put("nickName",adminInfo.getNickName());
        String token = JwtUtil.genToken(claims);
        return R.success(token);
    }
}




