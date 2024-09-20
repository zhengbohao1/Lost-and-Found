package com.it.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: appConfig
 * Description:
 * 用于从配置文件中读取应用程序的配置信息
 * @Author Joel
 * @Create 2024/9/19
 * @Version 1.0
 */

@Component
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /**
     * 发送邮件的用户名
     */
    @Value("${spring.mail.username}")
    private String sendUserName;

    /**
     * 管理员邮箱
     */
    @Value("${admin.emails}")
    private String adminEmails;

    /**
     * 项目文件根目录
     */
    @Value("${project.folder}")
    private String projectFolder;

    public String getSendUserName() {
        return sendUserName;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public String getProjectFolder() {
        return projectFolder;
    }
}
