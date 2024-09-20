package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.entity.UserInfo;
import com.it.enums.UserStatusEnum;
import com.it.exception.BusinessException;
import com.it.service.EmailCodeService;
import com.it.service.UserInfoService;
import com.it.mapper.UserInfoMapper;
import com.it.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author Yu
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2024-09-19 19:34:19
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private AppConfig appConfig;

    /**
     * 用户注册
     * @param email 邮箱
     * @param nickName 昵称
     * @param password 密码
     * @param emailCode 邮箱验证码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String nickName, String password, String emailCode) {
        // TODO：先校验邮箱和昵称是否已存在
        UserInfo emailUser = this.baseMapper.selectByEmail(email);
        if (emailUser != null) {
            throw new BusinessException("邮箱账号已经存在");
        }
        UserInfo nickNameUser = this.baseMapper.selectByNickName(nickName);
        if (nickNameUser != null) {
            throw new BusinessException("昵称已经存在");
        }
        // TODO：校验邮箱验证码
        emailCodeService.checkEmailCode(email, emailCode);
        // TODO：注册用户
        UserInfo userInfo = new UserInfo();
        String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
        userInfo.setUserId(userId);
        userInfo.setEmail(email);
        userInfo.setNickName(nickName);
        userInfo.setPassword(StringTools.encodeByMD5(password));
        userInfo.setJoinTime(new Date());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        this.baseMapper.insert(userInfo);
        throw new BusinessException("注册成功");
    }
}




