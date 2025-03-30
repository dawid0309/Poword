package com.poword.helper;

import com.google.gson.Gson;
import com.poword.dao.ArticleDao;
import com.poword.model.ArticleModel;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RSSHelper {
    private static ArticleDao articleDao = ArticleDao.getInstance();
    public static ArrayList<Map<String, String>> ReadTodayNews(String RSSUrl) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(RSSUrl);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();

                // ROME parsing logic as before
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(inputStream));

                System.out.println("Feed Title: " + feed.getTitle());

                ArrayList<Map<String, String>> newsList = new ArrayList<>();
                for (SyndEntry entry : feed.getEntries()) {
                    String description = entry.getDescription().getValue().replaceAll("<[^>]*>", ""); // 移除 HTML 标签
                    description = description.replaceAll("\\p{Zs}+", ""); // 移除所有空白字符
                    Date publishedDate = entry.getPublishedDate();
                    LocalDate publishedLocalDate = publishedDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                    LocalDate today = LocalDate.now();
                    Map<String, String> newsMap = new HashMap<>();
                    if (description.length() > 400 && publishedLocalDate.equals(today)) {
                        // 构建 JSON 数据
                        newsMap.put("title", entry.getTitle());
                        newsMap.put("content", description);
                        newsMap.put("generatedTime", publishedLocalDate.toString());
                        newsList.add(newsMap);
                    }
                }
                return newsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveRSSNews(ArrayList<Map<String, String>> rssnews) throws IOException {
        // 获取当前时间
        for (Map<String, String> news : rssnews) {
            String title = news.get("title");
            String content = news.get("content");
            String publishedData = news.get("generatedTime");

            ArticleModel articleModel = new ArticleModel();
            // 创建一个 RssNewsModel 对象
            articleModel.setTitle(title);
            articleModel.setGeneratedTime(LocalDate.parse(publishedData, DateTimeFormatter.ISO_DATE).atStartOfDay());
            articleModel.setArticleInChinese(content);

            // 插入到数据库
            boolean isInserted = articleDao.insertArticle(articleModel);
            if (isInserted) {
                System.out.println("News successfully inserted: " + title);
            } else {
                System.err.println("Failed to insert news: " + title);
            }
        }
    }

    public static List<String> getTodayNewsTitles(ArrayList<Map<String, String>> rssnews) throws IOException {
        List<String> todayNewsTitles = articleDao.queryBatchArticlesByDate(LocalDate.now().atStartOfDay());
        return todayNewsTitles;
    }
}


