//package com.it.config;
//
//import com.it.interceptors.LoginInterceptor;
//import jakarta.annotation.Resource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * ClassName: WebConfig
// * Description:
// * WebConfig配置类
// * @Author Joel
// * @Create 2024/9/21
// * @Version 1.0
// */
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Resource
//    private LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/checkCode","/user/sendEmailCode",
//                "/user/login","/user/register","/user/admin/login","/user/resetPwd","/user/getAvatarById");
//    }
//}
