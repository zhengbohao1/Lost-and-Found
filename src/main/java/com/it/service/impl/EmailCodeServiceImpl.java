package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.dto.SysSettingsDto;
import com.it.entity.EmailCode;
import com.it.entity.UserInfo;
import com.it.exception.BusinessException;
import com.it.mapper.UserInfoMapper;
import com.it.service.EmailCodeService;
import com.it.mapper.EmailCodeMapper;
import com.it.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author Yu
* @description 针对表【email_code】的数据库操作Service实现
* @createDate 2024-09-19 19:34:19
*/
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode>
    implements EmailCodeService{

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private AppConfig appConfig;

    @Resource
    private SysSettingsDto sysSettingsDto;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        // 如果是注册，先校验邮箱是否已存在
        if(type == Constants.ZERO){
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (userInfo != null) {
                throw new BusinessException("邮箱已被注册");
            }
        }
        // TODO: 发送QQ邮箱验证码
        // 生成五位随机验证码
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        sendEmailCode(email,code);
        // 将之前的发送的邮箱验证码失效
        this.baseMapper.disableEmailCode(email);
        EmailCode emailCode = new EmailCode();
        emailCode.setEmail(email);
        emailCode.setCode(code);
        // 状态0表示未使用，状态1表示已使用
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(new Date());
        this.baseMapper.insert(emailCode);
    }

    private void sendEmailCode(String email, String code){
        // TODO：发送邮箱验证码
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // MimeMessageHelper对象用于简化MimeMessage的设置过程
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 设置邮箱发件人
            helper.setFrom(appConfig.getSendUserName());
            // 设置邮箱收件人
            helper.setTo(email);
            // 获取系统设置,设置邮件主题和内容以及发送时间
            helper.setSubject(sysSettingsDto.getRegisterEmailTitle());
            helper.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
            helper.setSentDate(new Date());
            // 发送邮件
            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }
    }

    /**
     * 校验邮箱验证码
     * @param email
     * @param emailCode
     */
    @Override
    public void checkEmailCode(String email, String emailCode) {
        EmailCode emailCodeEntity = this.baseMapper.selectByEmailAndCode(email, emailCode);
        if (emailCodeEntity == null) {
            throw new BusinessException("邮箱验证码不正确");
        }
        if(emailCodeEntity.getStatus() == 1 ||
                System.currentTimeMillis()-emailCodeEntity.getCreateTime().getTime() > Constants.LENGTH_15 * 1000 * 60){
            throw new BusinessException("邮箱验证码已过期");
        }
        // 将邮箱验证码状态设置为已使用
        this.baseMapper.disableEmailCode(email);
    }
}




