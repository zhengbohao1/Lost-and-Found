package com.it.interceptors;

import com.it.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * ClassName: LoginInterceptor
 * Description:
 * 登录拦截器
 * 用于验证token令牌
 * @Author Joel
 * @Create 2024/9/21
 * @Version 1.0
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * 校验是否已登录
     * @param request
     * @param response
     * @param handler
     * @return true：放行，false：拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO: 验证token令牌
        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 放行
            return true;
        } catch (Exception e) {
            // http状态码设置为401，表示未登录
            response.setStatus(401);
            // 不放行
            return false;
        }

    }
}
