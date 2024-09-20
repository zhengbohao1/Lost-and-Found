package com.it.validation;

import com.it.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * ClassName: PasswordValidation
 * Description:
 * 密码校验规则
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

public class PasswordValidation implements ConstraintValidator<Password, String> {

    // 密码校验正则表达式：至少包含字母、数字，长度为6-18位
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d).{6,18}$";

    /**
     * 这个方法在验证器被初始化时调用，通常用于获取注解中的参数值，以便在验证逻辑中使用
     * 这个方法一般不需要用到
     * @param constraintAnnotation
     */
    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * 自定义校验规则
     * @param password
     * @param constraintValidatorContext
     * @return 校验通过返回true，校验失败返回false
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }
}
