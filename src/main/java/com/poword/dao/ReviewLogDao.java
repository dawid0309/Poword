package com.poword.dao;

import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.ReviewLogModel;
import lombok.Getter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewLogDao {

    @Getter
    private static ReviewLogDao instance = new ReviewLogDao();  // 单例

    private Connection conn;

    private ReviewLogDao() {
        this.conn = DatabaseConnectHelper.getInstance().getConnection();
    }

    // 插入复习记录
    public boolean insertReviewLog(int wordId, int result) {
        String sql = "INSERT INTO reviewlog (reviewid, wordid, result) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 使用当前时间戳作为 reviewid
            long reviewId = System.currentTimeMillis();
            pstmt.setLong(1, reviewId);
            pstmt.setInt(2, wordId);
            pstmt.setInt(3, result);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据 reviewid 查询复习日志
    public ReviewLogModel queryLogByReviewId(long reviewId) {
        ReviewLogModel reviewLog = null;
        String sql = "SELECT * FROM reviewlog WHERE reviewid = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setLong(1, reviewId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                reviewLog = new ReviewLogModel();
                reviewLog.setReviewId(resultSet.getLong("reviewid"));
                reviewLog.setWordId(resultSet.getInt("wordid"));
                reviewLog.setResult(resultSet.getInt("result"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewLog;
    }

    // 批量查询复习日志（根据 reviewIds）
    public List<ReviewLogModel> queryBatchLogsByReviewId(List<Long> reviewIds) {
        List<ReviewLogModel> result = new ArrayList<>();
        String sql = "SELECT * FROM reviewlog WHERE reviewid IN (" + String.join(",", Collections.nCopies(reviewIds.size(), "?")) + ")";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 设置参数
            for (int i = 0; i < reviewIds.size(); i++) {
                pstmt.setLong(i + 1, reviewIds.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                ReviewLogModel reviewLog = new ReviewLogModel();
                reviewLog.setReviewId(resultSet.getLong("reviewid"));
                reviewLog.setWordId(resultSet.getInt("wordid"));
                reviewLog.setResult(resultSet.getInt("result"));
                result.add(reviewLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 根据 wordId 查询复习日志（单条）
    public ReviewLogModel queryLogByWordId(int wordId) {
        ReviewLogModel reviewLog = null;
        String sql = "SELECT * FROM reviewlog WHERE wordid = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, wordId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                reviewLog = new ReviewLogModel();
                reviewLog.setReviewId(resultSet.getLong("reviewid"));
                reviewLog.setWordId(resultSet.getInt("wordid"));
                reviewLog.setResult(resultSet.getInt("result"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewLog;
    }

    // 批量查询复习日志（根据 wordIds）
    public List<ReviewLogModel> queryBatchLogsByWordId(List<Integer> wordIds) {
        List<ReviewLogModel> result = new ArrayList<>();
        // 将 wordIds 转换为字符串并构建 SQL 查询
        String sql = "SELECT * FROM reviewlog WHERE wordid IN (" + String.join(",", Collections.nCopies(wordIds.size(), "?")) + ")";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 设置参数
            for (int i = 0; i < wordIds.size(); i++) {
                pstmt.setInt(i + 1, wordIds.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                ReviewLogModel reviewLog = new ReviewLogModel();
                reviewLog.setReviewId(resultSet.getLong("reviewid"));
                reviewLog.setWordId(resultSet.getInt("wordid"));
                reviewLog.setResult(resultSet.getInt("result"));
                result.add(reviewLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
