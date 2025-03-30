package com.poword.helper;

import com.poword.model.UserBackground;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigHelper {

    private static final Properties properties = new Properties();

    // 静态代码块在类加载时执行，读取配置文件
    static {
        try (InputStream input = ConfigHelper.class.getClassLoader().getResourceAsStream("config/config.properties");
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {  // 指定UTF-8编码
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(reader);  // 使用InputStreamReader读取
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    // 静态方法，读取配置中的db.url
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getApiKey() {
        return properties.getProperty("api.key");
    }

    // 获取各个RSS的URL
    public static String getRssUrlPolitics() {
        return properties.getProperty("rss.url.politics");
    }

    public static String getRssUrlSociety() {
        return properties.getProperty("rss.url.society");
    }

    public static String getRssUrlLegal() {
        return properties.getProperty("rss.url.legal");
    }

    public static String getRssUrlWorld() {
        return properties.getProperty("rss.url.world");
    }

    public static String getRssUrlHaixia() {
        return properties.getProperty("rss.url.haixia");
    }

    public static String getRssUrlMilitary() {
        return properties.getProperty("rss.url.military");
    }

    public static String getRssUrlYwkx() {
        return properties.getProperty("rss.url.ywkx");
    }

    public static UserBackground getUserBackground() {
        // 读取配置项并设置属性
        UserBackground userBackground = new UserBackground(properties.getProperty("user.focus_area"),
            Integer.parseInt(properties.getProperty("user.age")), properties.getProperty("user.occupation"),
            properties.getProperty("user.education"), properties.getProperty("user.style_preference"));
        return userBackground;
    }
}
