package com.poword.dao;

import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.ArticleModel;
import lombok.Getter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleDao {

    @Getter
    private static ArticleDao instance = new ArticleDao();  // 单例

    private Connection conn;

    private ArticleDao() {
        this.conn = DatabaseConnectHelper.getInstance().getConnection();
    }

    // 根据 ID 查询文章
    public ArticleModel queryArticleById(long id) {
        ArticleModel article = null;
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                article = new ArticleModel();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setGeneratedTime(resultSet.getTimestamp("generatedtime").toLocalDateTime());
                article.setArticleInChinese(resultSet.getString("articleinchinese"));
                article.setArticleInEnglish(resultSet.getString("articleinenglish"));
                article.setInsertedWord(resultSet.getString("insertedword"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 插入一篇文章
    public boolean insertArticle(ArticleModel article) {
        String sql = "INSERT INTO articles (title, generatedtime, articleinchinese, articleinenglish, insertedword) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setTimestamp(2, Timestamp.valueOf(article.getGeneratedTime()));
            pstmt.setString(3, article.getArticleInChinese());
            pstmt.setString(4, article.getArticleInEnglish());
            pstmt.setString(5, article.getInsertedWord());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("A UNIQUE constraint failed")){
                System.out.println("文章已存在");
            }else {
                e.printStackTrace();
            }
            return false;
        }
    }


    // 根据标题查询文章
    public List<ArticleModel> queryArticlesByTitle(String title) {
        List<ArticleModel> result = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE title LIKE ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + title + "%");
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                ArticleModel article = new ArticleModel();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setGeneratedTime(resultSet.getTimestamp("generatedtime").toLocalDateTime());
                article.setArticleInChinese(resultSet.getString("articleinchinese"));
                article.setArticleInEnglish(resultSet.getString("articleinenglish"));
                article.setInsertedWord(resultSet.getString("insertedword"));
                result.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 批量查询文章（根据 ID 列表）
    public List<ArticleModel> queryBatchArticlesByTitles(List<String> titles) {
        List<ArticleModel> result = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE title IN (" + String.join(",", Collections.nCopies(titles.size(), "?")) + ")";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 设置查询参数
            for (int i = 0; i < titles.size(); i++) {
                pstmt.setString(i + 1, titles.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                ArticleModel article = new ArticleModel();
                article.setId(resultSet.getLong("id"));
                article.setTitle(resultSet.getString("title"));
                article.setGeneratedTime(resultSet.getTimestamp("generatedtime").toLocalDateTime());
                article.setArticleInChinese(resultSet.getString("articleinchinese"));
                article.setArticleInEnglish(resultSet.getString("articleinenglish"));
                article.setInsertedWord(resultSet.getString("insertedword"));
                result.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> queryBatchArticlesByDate(LocalDateTime date) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT title FROM articles WHERE generatedtime = ? ";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 设置查询参数
            pstmt.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 更新文章内容
    public boolean updateArticle(ArticleModel article) {
        String sql = "UPDATE articles SET title = ?, generatedtime = ?, articleinchinese = ?, articleinenglish = ?, insertedword = ? WHERE id = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setTimestamp(2, Timestamp.valueOf(article.getGeneratedTime()));
            pstmt.setString(3, article.getArticleInChinese());
            pstmt.setString(4, article.getArticleInEnglish());
            pstmt.setString(5, article.getInsertedWord());
            pstmt.setLong(6, article.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
