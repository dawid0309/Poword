package com.poword.service.agentservice.worker;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.helper.AIHelper;
import com.poword.helper.GlobalGson;
import com.poword.helper.PromptHelper;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

public class Translator implements Handler {
    private Handler nextHandler;
    private Handler previousHandler;

    @Override
    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void setPreviousHandler(Handler handler) {
        this.previousHandler = handler;
    }

    @Override
    public void handleRequest(NewsDTO request) throws NoApiKeyException, InputRequiredException {
        String callingResult;
        if (request.isNeedRetranslate() && request.getAdvice() != null){
            callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("translator_prompt"),
                String.format("请根据以下建议重新翻译这篇文章。\\n\\n## 提供的建议\\n%s\\n\\n## 需要重新翻译的文章\\n%s\\n\\n请确保翻译后的文章符合提供的建议，并且保持文章的核心信息和要点不变。",
                    request.getAdvice(),request.getOptimizedArticle()));
        }else {
            callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("translator_prompt"), request.getOptimizedArticle());
        }
        NewsDTO result = GlobalGson.getInstance().fromJson(callingResult, NewsDTO.class);
        request.setArticleEnglish(result.getArticleEnglish());
        this.nextHandler.handleRequest(request);
    }
}
