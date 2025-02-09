package com.poword.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.SynosModel;

public class SynosDao {

    private Connection conn = DatabaseConnectHelper.getInstance().getConnection();

    public void createRecord(SynosModel synosModel) {
        String sql = "INSERT INTO synos (id, word, synos, updatetime, createdtime, source) VALUES(?, ?, ?, datetime('now'), datetime('now'), ?)";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, synosModel.getId());
            pstmt.setString(2, synosModel.getWord());
            pstmt.setString(3, synosModel.getSynos()); // Ensure this is a valid JSON string
            pstmt.setString(4, synosModel.getSource());
            pstmt.execute();
            System.out.println("Record created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Read a record by ID
    public void getRecord(int id) {
        String sql = "SELECT * FROM synos WHERE id = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Word: " + rs.getString("word"));
                System.out.println("Synos: " + rs.getString("synos"));
                System.out.println("Update Time: " + rs.getString("updatetime"));
                System.out.println("Created Time: " + rs.getString("createdtime"));
                System.out.println("Source: " + rs.getString("source"));
            }
        } catch (SQLException e) {
            System.err.println("Error reading record: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Update a record by ID
    public void updateRecord(int id, String word, String synos, String source) {
        String sql = "UPDATE synos SET word = ?, synos = ?, source = ?, updatetime = datetime('now') WHERE id = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            pstmt.setString(2, synos);
            pstmt.setString(3, source);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Record updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete a record by ID
    public void deleteRecord(int id) {
        String sql = "DELETE FROM synos WHERE id = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting record: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
