package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.entity.GoldCoin;
import com.it.entity.UserInfo;
import com.it.enums.UserStatusEnum;
import com.it.exception.BusinessException;
import com.it.service.EmailCodeService;
import com.it.service.GoldCoinService;
import com.it.service.UserInfoService;
import com.it.mapper.UserInfoMapper;
import com.it.utils.JwtUtil;
import com.it.utils.StringTools;
import com.it.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private GoldCoinService goldcoinService;

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
        //TODO：生成用户ID并检查唯一性
        userInfo.setUserId(userId);
        UserInfo idUser = getById(userId);
        while (idUser != null) {
            userId = StringTools.getRandomNumber(Constants.LENGTH_10);
            idUser = getById(userId);
        }
        userInfo.setEmail(email);
        userInfo.setNickName(nickName);
        userInfo.setPassword(StringTools.encodeByMD5(password));
        userInfo.setJoinTime(new Date());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        this.baseMapper.insert(userInfo);
        GoldCoin goldCoin = new GoldCoin();
        goldCoin.setUserId(userId);
        goldCoin.setAmount(0);
        goldCoin.setEmail(email);
        goldcoinService.save(goldCoin);
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

    /**
     * 更新用户密码
     * @param oldPwd
     * @param newPwd
     * @param rePwd
     * @return
     */
    @Override
    public void updatePwd(String oldPwd, String newPwd, String rePwd) {
        // TODO: 校验原密码是否正确
        // 从ThreadLocal中获取用户nickName
        Map<String,Object> claims = ThreadLocalUtil.get();
        String nickName = (String) claims.get("nickName");
        UserInfo loginUser = this.baseMapper.selectByNickName(nickName);
        if (!StringTools.checkPassword(oldPwd, loginUser.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        //校验新密码和确认密码是否一致
        if(!newPwd.equals(rePwd)) {
            throw new BusinessException("两次密码不一致");
        }
        // TODO: 校验成功，更新密码
        //用ThreadLocal获取userId
        String userId = (String) claims.get("userId");
        this.baseMapper.updatePwd(StringTools.encodeByMD5(newPwd), userId);
    }

    /**
     * 忘记密码时的重置密码
     * @param email
     * @param password
     * @param emailCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        // TODO: 校验邮箱是否存在
        UserInfo userInfo = this.baseMapper.selectByEmail(email);
        if(userInfo == null){
            throw new BusinessException("邮箱账号不存在");
        }
        // TODO: 校验邮箱验证码
        emailCodeService.checkEmailCode(email, emailCode);
        // TODO: 更新密码
        this.baseMapper.updatePwdByEmail(StringTools.encodeByMD5(password), email);
    }

    /**
     * 获取所有用户信息
     * @return
     */
    @Override
    public List<UserInfo> getAllUserInfo() {
        return this.baseMapper.selectAllUserInfo();
    }

    @Override
    public void disableUser(String userId) {
        // 检查用户是否存在以及是否尚未被禁用
        UserInfo userInfo = this.baseMapper.selectById(userId);
        if (userInfo == null || userInfo.getStatus() == UserStatusEnum.DISABLE.getStatus()) {
            throw new BusinessException("用户不存在或已被禁用");
        }
        this.baseMapper.disableUser(userId);
    }

    @Override
    public String getUserNameById(String userId) {
        UserInfo userInfo = getById(userId);
        return userInfo.getNickName();
    }

}




