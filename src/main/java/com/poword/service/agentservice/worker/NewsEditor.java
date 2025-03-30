package com.poword.service.agentservice.worker;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.helper.*;
import com.poword.model.UserBackground;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

public class NewsEditor implements Handler {
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
        String userPrompt = generateUserPrompt(request);

        String callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("news_editor_prompt"), userPrompt);
        NewsDTO result = GlobalGson.getInstance().fromJson(callingResult, NewsDTO.class);

        request.setOptimizedArticle(result.getOptimizedArticle());
        this.nextHandler.handleRequest(request);
    }

    public static String generateUserPrompt(NewsDTO request){
        UserBackground userBackground = request.getUserBackground();
        String article = request.getRawArticle();
        String userPrompt = String.format("用户背景：%s\n请根据以下新闻内容，生成一个适合读者阅读的新闻内容:\n%s", userBackground.toString(), article);

        return userPrompt;
    }
}
