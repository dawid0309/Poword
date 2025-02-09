package com.poword.dao;

import com.poword.helper.ConfigHelper;
import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.UserWordInfoModel;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserWordInfoDao {
    @Getter
    public static UserWordInfoDao instance = new UserWordInfoDao();
    private Connection conn;

    // 获取数据库连接
    private UserWordInfoDao() {
        this.conn = DatabaseConnectHelper.getInstance().getConnection();
    }

    // 插入一条记录
    public void insertUserWordInfo(UserWordInfoModel userWordInfoModel) {
        String sql = "INSERT OR IGNORE INTO userwordinfo (wordid, highlighted, favorited, annotation, updatetime, createdtime) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userWordInfoModel.getId());
            pstmt.setBoolean(2, userWordInfoModel.isHighlighted());
            pstmt.setBoolean(3, userWordInfoModel.isFavorited());
            pstmt.setString(4, userWordInfoModel.getAnnotation());
            pstmt.setTimestamp(5, new Timestamp(userWordInfoModel.getUpdateTime().getTime()));
            pstmt.setTimestamp(6, new Timestamp(userWordInfoModel.getCreateTime().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据ID查询记录
    public UserWordInfoModel getUserWordInfoById(int id) {
        String sql = "SELECT * FROM userwordinfo WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUserWordInfoModel(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查询所有记录
    public List<UserWordInfoModel> getAllUserWordInfo() {
        List<UserWordInfoModel> userWordInfoModelList = new ArrayList<>();
        String sql = "SELECT * FROM userwordinfo";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                userWordInfoModelList.add(mapResultSetToUserWordInfoModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userWordInfoModelList;
    }

    // 更新记录
    public void updateUserWordInfo(UserWordInfoModel userWordInfoModel) {
        String sql = "UPDATE userwordinfo SET highlighted = ?, favorited = ?, annotation = ?, updatetime = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, userWordInfoModel.isHighlighted());
            pstmt.setBoolean(2, userWordInfoModel.isFavorited());
            pstmt.setString(3, userWordInfoModel.getAnnotation());
            pstmt.setTimestamp(4, new Timestamp(userWordInfoModel.getUpdateTime().getTime()));
            pstmt.setInt(5, userWordInfoModel.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除记录
    public void deleteUserWordInfo(int id) {
        String sql = "DELETE FROM userwordinfo WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 将ResultSet映射到UserWordInfoModel对象
    private UserWordInfoModel mapResultSetToUserWordInfoModel(ResultSet rs) throws SQLException {
        UserWordInfoModel userWordInfoModel = new UserWordInfoModel();
        userWordInfoModel.setId(rs.getInt("id"));
        userWordInfoModel.setHighlighted(rs.getBoolean("highlighted"));
        userWordInfoModel.setFavorited(rs.getBoolean("favorited"));
        userWordInfoModel.setAnnotation(rs.getString("annotation"));
        userWordInfoModel.setUpdateTime(rs.getTimestamp("updatetime"));
        userWordInfoModel.setCreateTime(rs.getTimestamp("createdtime"));
        return userWordInfoModel;
    }
}
