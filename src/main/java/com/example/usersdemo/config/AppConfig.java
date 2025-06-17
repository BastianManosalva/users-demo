package com.example.usersdemo.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private HashMap<String, String> regularExpression;

    public HashMap<String, String> getRegularExpression() {
        return regularExpression;
    }
}
