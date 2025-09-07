package com.petstore.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ApiConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url", "https://petstore.swagger.io/v2");
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "30000"));
    }

    public static boolean isLogEnabled() {
        return Boolean.parseBoolean(properties.getProperty("api.log.enabled", "true"));
    }
}
