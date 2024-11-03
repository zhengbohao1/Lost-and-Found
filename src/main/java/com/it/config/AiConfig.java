package com.it.config;

import com.zhipu.oapi.ClientV4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 智谱AI配置类
 */
@Configuration
public class AiConfig {

    private final String apiKey = "8d163d84853448ba5ef7e786bbfded90.Ij5mavb8gYk4XI4n";  // 直接赋值

    @Bean
    public ClientV4 getClientV4() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key cannot be null or empty");
        }
        return new ClientV4.Builder(apiKey).build();
    }
}
