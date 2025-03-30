package com.poword.service.agentservice;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.reflect.TypeToken;
import com.poword.dao.ArticleDao;
import com.poword.helper.*;
import com.poword.model.ArticleModel;
import com.poword.model.UserBackground;
import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.evaluator.LanguageEvaluator;
import com.poword.service.agentservice.evaluator.WordEvaluator;
import com.poword.service.agentservice.worker.NewsEditor;
import com.poword.service.agentservice.worker.Questioner;
import com.poword.service.agentservice.worker.Translator;
import com.poword.service.agentservice.worker.WordInsertor;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewsCommander {

    public static void startWriting(String rssFeedUrl, String[] wordsToReview) throws NoApiKeyException, InputRequiredException, IOException {
        ArrayList<NewsDTO> newsDTOS = selectNews(rssFeedUrl);
        Handler chain = buildChain();
        for (NewsDTO newsDTO : newsDTOS) {
            newsDTO.setUserBackground(UserBackground.getInstance());
            newsDTO.setWordsToInsert(wordsToReview);
            chain.handleRequest(newsDTO);
            saveArticle(newsDTO);
            wordsToReview = Arrays.stream(wordsToReview)
                .filter(word -> !Arrays.asList(newsDTO.getWordsInserted()).contains(word))
                .toArray(String[]::new);

        }
    }

    private static void saveArticle(NewsDTO newsDTO) {
        ArticleModel articleModel = new ArticleModel();
        articleModel.setTitle(newsDTO.getTitle());
        articleModel.setArticleInChinese(newsDTO.getOptimizedArticle());
        articleModel.setArticleInEnglish(newsDTO.getArticleEnglish());
        articleModel.setGeneratedTime(LocalDateTime.now());
        articleModel.setInsertedWord(String.join(", ", newsDTO.getWordsToInsert()));
        ArticleDao.getInstance().insertArticle(articleModel);
    }

    public static ArrayList<NewsDTO> selectNews(String rssFeedUrl) throws NoApiKeyException, InputRequiredException, IOException {
        ArrayList<Map<String, String>> newsList = RSSHelper.ReadTodayNews(rssFeedUrl);
        RSSHelper.saveRSSNews(newsList);
        List<String> todayNewsTitles = RSSHelper.getTodayNewsTitles(newsList);

        String callingResult = AIHelper.callAliWithMessage(PromptHelper
            .getPrompt("news_selector_prompt"), "我需要你评估的标题如下：\n" + String.join("\n", todayNewsTitles));

        ArrayList<NewsDTO> newsDTOS = GlobalGson.getInstance().fromJson(callingResult, new TypeToken<ArrayList<NewsDTO>>(){}.getType());
        newsDTOS.removeIf(newsDTO -> newsDTO.getTitle() == null || newsDTO.getTitle().isEmpty() || !newsDTO.isHighQuality());
        // 根据 title 批量查询所有文章
        //todo:不用标题拿文章
        List<ArticleModel> articles = ArticleDao.getInstance().queryBatchArticlesByTitles(newsDTOS.stream().map(NewsDTO::getTitle).collect(Collectors.toList()));

        Map<String, String> articlesMap = articles.stream()
            .collect(Collectors.toMap(ArticleModel::getTitle, ArticleModel::getArticleInChinese));

        newsDTOS.forEach(newsDTO -> {
            String article = articlesMap.get(newsDTO.getTitle());
            if (article != null) {
                newsDTO.setRawArticle(article);
                newsDTO.setUserBackground(UserBackground.getInstance());
            }
        });

        return newsDTOS;
    }

    public static Handler buildChain() {
        Handler newsEditor = new NewsEditor();
        Handler translator = new Translator();
        Handler languageEvaluator = new LanguageEvaluator();
        Handler wordInsertor = new WordInsertor();
        Handler wordEvaluator = new WordEvaluator();
        Handler questioner = new Questioner();

        // 链接责任链
        newsEditor.setNextHandler(translator);
        translator.setNextHandler(languageEvaluator);
        languageEvaluator.setNextHandler(wordInsertor);
        wordInsertor.setNextHandler(wordEvaluator);
        wordEvaluator.setNextHandler(questioner);

        // 设置前后连接
        questioner.setPreviousHandler(wordEvaluator);
        wordEvaluator.setPreviousHandler(wordInsertor);
        wordInsertor.setPreviousHandler(languageEvaluator);
        languageEvaluator.setPreviousHandler(translator);
        translator.setPreviousHandler(newsEditor);

        return newsEditor;  // 返回责任链的第一个处理器
    }


    public static void main(String[] args) throws IOException, NoApiKeyException, InputRequiredException {
        String words = "completely\n" +
            "complex\n" +
            "complicate\n" +
            "complicated\n" +
            "computer\n" +
            "concentrate\n" +
            "concentration\n" +
            "concept\n" +
            "concern\n" +
            "concerned\n" +
            "concerning\n" +
            "concert\n" +
            "conclude\n" +
            "conclusion\n" +
            "concrete\n" +
            "condition\n" +
            "conduct\n" +
            "conference\n" +
            "confidence\n" +
            "confident\n" +
            "confidently\n" +
            "confine\n" +
            "confined\n" +
            "confirm\n" +
            "conflict\n" +
            "confront\n" +
            "confuse\n" +
            "confused\n" +
            "confusing\n" +
            "confusion\n" +
            "congratulations\n" +
            "congress\n" +
            "connect\n" +
            "connection\n" +
            "conscious";
        String[] wordToReview = words.split("\n");
        startWriting(ConfigHelper.getRssUrlHaixia(), wordToReview);
    }
}
