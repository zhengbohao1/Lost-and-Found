package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.entity.UserInfo;
import com.it.enums.UserStatusEnum;
import com.it.exception.BusinessException;
import com.it.service.EmailCodeService;
import com.it.service.UserInfoService;
import com.it.mapper.UserInfoMapper;
import com.it.utils.JwtUtil;
import com.it.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    }

    /**
     * 普通用户登录
     * @param email
     * @param password
     */
    @Override
    public R<String> login(String email, String password) {
        // TODO：校验用户是否存在、密码是否正确、用户的账户是否被禁用
        UserInfo userInfo = this.baseMapper.selectByEmail(email);
        if(userInfo == null || !userInfo.getPassword().equals(StringTools.encodeByMD5(password))){
            throw new BusinessException("邮箱或密码错误");
        }
        if(userInfo.getStatus() == UserStatusEnum.DISABLE.getStatus()){
            throw new BusinessException("账户已被禁用");
        }
        // TODO: 登录成功后更新用户登录时间
        this.baseMapper.updateLastLoginTimeById(userInfo.getUserId(), new Date());
        // TODO: 将用户信息存储到token令牌中
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUserId());
        claims.put("nickName", userInfo.getNickName());
        String token = JwtUtil.genToken(claims);
        return R.success(token);
    }

    /**
     * 根据昵称获取用户信息
     * @param nickName
     * @return
     */
    @Override
    public R<UserInfo> getUserInfo(String nickName) {
        UserInfo userInfo = this.baseMapper.selectByNickName(nickName);
        return R.success(userInfo);
    }

}




