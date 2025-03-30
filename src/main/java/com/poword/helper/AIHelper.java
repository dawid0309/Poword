package com.poword.helper;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.util.Arrays;

public class AIHelper {
    public static String callAliWithMessage(String systemMsgContent, String userMsgContent) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content(systemMsgContent)
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content(userMsgContent)
            .build();
        GenerationParam param = GenerationParam.builder()
            .apiKey(ConfigHelper.getApiKey())
            .model("qwen-plus")
            .messages(Arrays.asList(systemMsg, userMsg))
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .build();
        return gen.call(param).getOutput().getChoices().get(0).getMessage().getContent();
    }
}
