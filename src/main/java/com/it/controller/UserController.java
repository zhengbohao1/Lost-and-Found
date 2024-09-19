package com.it.controller;

import com.it.constants.Constants;
import com.it.utils.CreateImageCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * ClassName: UserController
 * Description:
 * 用户相关控制层
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

    /**
     * 生成验证码图像
     * 生成一个验证码图像，并将验证码字符串存储在会话中，以便后续验证用户输入的验证码是否正确。
     * 通过设置HTTP响应头，确保浏览器不会缓存这个图像，每次请求都会生成一个新的验证码图像。
     * @param response 用于设置HTTP响应头和将生成的图像写入响应流
     * @param session 用于在会话中存储生成的验证码
     * @param type 可选参数，用于区分验证码的类型，0表示登录注册之前的验证码，1表示邮箱验证码发送之前的验证码
     * @throws IOException
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }



}
