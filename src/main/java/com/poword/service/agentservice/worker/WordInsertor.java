package com.poword.service.agentservice.worker;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.helper.AIHelper;
import com.poword.helper.GlobalGson;
import com.poword.helper.PromptHelper;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

public class WordInsertor implements Handler {
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
        // 改写新闻
        String callingResult;
        if (request.isNeedReinsert()){
            callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("word_insertor_prompt"),
                String.format("请根据以下建议重新将我提供的单词插入这篇文章。\n\n## 提供的建议\n%s\n\n## 需要插入的单词\n%s\n\n## 需要修改的新闻\n%s" +
                        "\n\n请确保修改后的文章准确插入了我提供的所有单词，并且保持文章的核心信息和要点不变。",
                    request.getAdvice(),request.getWordsToInsert(),request.getArticleEnglish()));
        }else {
            callingResult = AIHelper.callAliWithMessage(PromptHelper.getPrompt("word_insertor_prompt"),
                String.format("## 需要插入的单词\n%s\n\n## 待插入单词的新闻\n%s" +
                    "\n\n请确保修改后的文章准确插入了我提供的所有单词，并且保持文章的核心信息和要点不变。",
                request.getWordsToInsert(),request.getArticleEnglish()));
        }
        NewsDTO result = GlobalGson.getInstance().fromJson(callingResult, NewsDTO.class);
        request.setArticleEnglish(result.getArticleEnglish());
        this.nextHandler.handleRequest(request);
    }
}
