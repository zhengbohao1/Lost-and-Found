package com.it.annotation;

import com.it.validation.PasswordValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * ClassName: Password
 * Description:
 *  自定义密码参数校验注解
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@Documented
@Constraint(validatedBy = PasswordValidation.class)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "密码只能是数字、字母、特殊字符组成，长度为8-18位";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
