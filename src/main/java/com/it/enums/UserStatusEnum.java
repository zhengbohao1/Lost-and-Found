package com.it.enums;

/**
 * ClassName: UserStatusEnum
 * Description:
 * 用户状态枚举类
 * @Author Joel
 * @Create 2024/9/2 15:24
 * @Version 1.0
 */

public enum UserStatusEnum {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
