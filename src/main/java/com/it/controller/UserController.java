package com.it.controller;

import com.it.common.R;
import com.it.config.AppConfig;
import com.it.constants.Constants;
import com.it.exception.BusinessException;
import com.it.query.EmailQuery;
import com.it.query.UserQuery;
import com.it.service.AdminInfoService;
import com.it.service.EmailCodeService;
import com.it.service.UserInfoService;
import com.it.utils.CreateImageCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * ClassName: UserController
 * Description:
 * 用户账号相关控制层
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private AdminInfoService adminInfoService;

    @Resource
    private AppConfig appConfig;

    /**
     * 生成验证码图像
     * 生成一个验证码图像，并将验证码字符串存储在会话中，以便后续验证用户输入的验证码是否正确。
     * 通过设置HTTP响应头，确保浏览器不会缓存这个图像，每次请求都会生成一个新的验证码图像。
     * @param response 用于设置HTTP响应头和将生成的图像写入响应流
     * @param session 用于在会话中存储生成的验证码
     * @throws IOException
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, code);
        vCode.write(response.getOutputStream());
    }

    /**
     * 发送邮箱验证码
     * @param emailQuery
     * emailQuery中有type属性，0表示注册、1表示找回密码
     * @return
     */
    @PostMapping("/sendEmailCode")
    public R<String> sendEmailCode(@RequestBody @Validated EmailQuery emailQuery){
        String email = emailQuery.getEmail();
        Integer type = emailQuery.getType();
        // 发送邮箱验证码
        emailCodeService.sendEmailCode(email,type);
        return R.success(null);
    }

    /**
     * 用户注册
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping ("/register")
    public R<String> register(HttpSession session, @RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String nickName = userQuery.getNickName();
        String password = userQuery.getPassword();
        String emailCode = userQuery.getEmailCode();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行注册
            userInfoService.register(email, nickName, password, emailCode);
            return R.success(null);
        } finally {
            // 清除会话中存储的验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * 普通用户登录
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping("/login")
    public R<String> login(HttpSession session,@RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String password = userQuery.getPassword();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行登录
            return userInfoService.login(email, password);
        } finally {
            // 清除会话中存储的图片验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    /**
     * 管理员登录登录
     * @param session
     * @param userQuery
     * @return
     */
    @PostMapping("/admin/login")
    public void adminLogin(HttpSession session,@RequestBody @Validated UserQuery userQuery){
        String email = userQuery.getEmail();
        String password = userQuery.getPassword();
        String checkCode = userQuery.getCheckCode();
        try {
            // 校验图片验证码
            if(!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            // TODO: 进行登录
            adminInfoService.adminLogin(email, password);
        } finally {
            // 清除会话中存储的图片验证码
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

}
