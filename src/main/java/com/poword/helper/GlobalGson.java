package com.poword.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GlobalGson {
    // 创建一个私有的静态全局Gson实例
    private static final Gson gson = new GsonBuilder().create();

    // 私有的构造方法，防止外部实例化
    private GlobalGson() {}

    // 提供一个公共的静态方法来获取Gson实例
    public static Gson getInstance() {
        return gson;
    }
}
