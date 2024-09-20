package com.it.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: CheckAdmin
 * Description:
 * 用于校验是否是管理员的注解
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAdmin {
}
