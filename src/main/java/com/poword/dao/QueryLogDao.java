package com.poword.dao;

import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.QueryLogModel;
import lombok.Getter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryLogDao {

    @Getter
    private static QueryLogDao instance = new QueryLogDao();  // 单例

    private Connection conn;

    private QueryLogDao() {
        this.conn = DatabaseConnectHelper.getInstance("src\\main\\java\\com\\poword\\resources\\db\\stardict.db").getConnection();
    }

    public QueryLogModel queryLogByQueryId(long queryId) {
        QueryLogModel queryLog = null;
        String sql = "SELECT * FROM querylog WHERE queryid = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setLong(1, queryId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                queryLog = new QueryLogModel();
                queryLog.setQueryId(resultSet.getLong("queryid"));
                queryLog.setWordId(resultSet.getInt("wordid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryLog;
    }


    // 插入查询记录
    public boolean insertQueryLog(int wordId) {
        String sql = "INSERT INTO querylog (queryid, wordid) VALUES (?, ?)";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 使用当前时间戳作为 queryid
            long queryId = System.currentTimeMillis();
            pstmt.setLong(1, queryId);
            pstmt.setInt(2, wordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 批量查询日志
    public List<QueryLogModel> queryBatchLogsByQueryId(List<Long> queryIds) {
        List<QueryLogModel> result = new ArrayList<>();
        String sql = "SELECT * FROM querylog WHERE queryid IN (" + String.join(",", (CharSequence) queryIds) + ")";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                QueryLogModel queryLog = new QueryLogModel();
                queryLog.setQueryId(resultSet.getLong("queryid"));
                queryLog.setWordId(resultSet.getInt("wordid"));
                result.add(queryLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public QueryLogModel queryLogByWordId(int wordId) {
        QueryLogModel queryLog = null;
        String sql = "SELECT * FROM querylog WHERE wordid = ?";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, wordId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                queryLog = new QueryLogModel();
                queryLog.setQueryId(resultSet.getLong("queryid"));
                queryLog.setWordId(resultSet.getInt("wordid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryLog;
    }


    public List<QueryLogModel> queryBatchLogsByWordId(List<Integer> wordIds) {
        List<QueryLogModel> result = new ArrayList<>();
        // 将 wordIds 转换为字符串并构建 SQL 查询
        String sql = "SELECT * FROM querylog WHERE wordid IN (" + String.join(",", Collections.nCopies(wordIds.size(), "?")) + ")";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            // 设置参数
            for (int i = 0; i < wordIds.size(); i++) {
                pstmt.setInt(i + 1, wordIds.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                QueryLogModel queryLog = new QueryLogModel();
                queryLog.setQueryId(resultSet.getLong("queryid"));
                queryLog.setWordId(resultSet.getInt("wordid"));
                result.add(queryLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
