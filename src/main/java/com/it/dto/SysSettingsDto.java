package com.it.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ClassName: SysSettingsDto
 * Description:
 * 存储系统设置的相关信息
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Component
public class SysSettingsDto implements Serializable {

    private String registerEmailTitle = "邮箱验证码";

    private String registerEmailContent = "您好，您的邮箱验证码是：%s，15分钟有效";

}
