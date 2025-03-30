package com.poword.service.agentservice.evaluator;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.helper.AIHelper;
import com.poword.helper.GlobalGson;
import com.poword.helper.PromptHelper;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordEvaluator implements Handler {
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
            String.format("## 需要插入的单词\n%s\n\n## 需要检查单词插入情况的新闻\n%s" +
                    "\n\n请严格检查文章准确插入了我提供的所有单词，并且保持文章的核心信息和要点不变。",
                request.getWordsToInsert(),request.getArticleEnglish()));
        NewsDTO result = GlobalGson.getInstance().fromJson(callingResult, NewsDTO.class);
        request.setNeedReinsert(result.isNeedReinsert());
        request.setAdvice(result.getAdvice());
        request.setWordsInserted(getInsertedWords(request));
        if (!request.isNeedReinsert() || request.getAdvice() != null){
            this.previousHandler.handleRequest(request);
        }
        this.nextHandler.handleRequest(request);
    }

    public static String[] getInsertedWords(NewsDTO request) {
        // 给定的带有插入词汇的文本
        String text = request.getArticleEnglish();
        // 定义正则表达式来匹配 <<>> 中的词汇
        Pattern pattern = Pattern.compile("<<(\\w+)>>");
        Matcher matcher = pattern.matcher(text);

        // 用于存储提取出的词汇
        List<String> extractedWords = new ArrayList<>();

        // 找到所有匹配的词汇
        while (matcher.find()) {
            // 提取词汇并添加到列表中
            extractedWords.add(matcher.group(1));
        }

        // 将 List<String> 转换为 String[]
        return extractedWords.toArray(new String[0]);
    }
}
