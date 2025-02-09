package com.poword.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {

    private static final Properties properties = new Properties();

    // 静态代码块在类加载时执行，读取配置文件
    static {
        try (InputStream input = ConfigHelper.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    // 静态方法，读取配置中的db.url
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }
}
