package com.poword.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PromptHelper {
    public static String getPrompt(String promptName) {
        Gson gson = GlobalGson.getInstance();
        String promptValue = null;

        // 通过 ClassLoader 获取资源文件的输入流
        try (InputStream inputStream = PromptHelper.class.getClassLoader().getResourceAsStream("config/ai_prompts.json")) {
            if (inputStream == null) {
                System.out.println("无法找到文件：config/ai_prompts.json");
                return null;
            }

            // 使用 InputStreamReader 和 Gson 解析 JSON
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                // 获取 JSON 对象中的 prompt 字段
                promptValue = jsonObject.get(promptName).getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promptValue;
    }
}
