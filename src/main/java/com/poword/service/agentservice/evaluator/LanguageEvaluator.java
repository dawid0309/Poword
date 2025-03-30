package com.poword.service.agentservice.evaluator;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.helper.AIHelper;
import com.poword.helper.GlobalGson;
import com.poword.helper.PromptHelper;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

public class LanguageEvaluator implements Handler {
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
        // 根据用户背景检查文章的难度
        String callingResult;
        callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("translator_prompt"),
                String.format("请根据以下信息评估这篇文章。\\n\\n## 用户的英语能力：\\n%s\\n\\n## 需要评估的文章\\n%s\\n\\n",
                    request.getUserBackground(),request.getArticleEnglish()));
        NewsDTO result = GlobalGson.getInstance().fromJson(callingResult, NewsDTO.class);
        request.setNeedRetranslate(result.isNeedRetranslate());
        request.setAdvice(result.getAdvice());
        if (!request.isNeedReinsert() || request.getAdvice() != null){
            this.previousHandler.handleRequest(request);
        }
//        this.nextHandler.handleRequest(request);
    }
}
