package com.poword.dao;
import java.sql.*;
import java.util.*;

import lombok.Getter;

import com.poword.model.WordBaseModel;
import com.poword.model.WordDetailModel;
import com.poword.model.WordModel;
import com.poword.helper.DatabaseConnectHelper;

public class DictDao {

    private Connection conn;
    @Getter
    private static DictDao instance = new DictDao();

    public DictDao() {
        this.conn = DatabaseConnectHelper.getInstance().getConnection();
    }

    /**
     * @param word
     * @return
     */
    public int getIdByWord(String word){
        String sql = "SELECT id FROM dict WHERE word = ?";
        int id = 0;
        try(PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("Error reading record: " + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }


    public WordBaseModel query(Object key, boolean detailed) {
        String sql;
        ResultSet record = null;
        WordBaseModel wordModel = null;
        try {
            if (key instanceof Integer) {
                sql = "SELECT * FROM dict WHERE id = ?;";
            } else if (key instanceof String) {
                sql = "SELECT * FROM dict WHERE word = ?;";
            } else {
                return null;
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setObject(1, key);
            record = pstmt.executeQuery();

            if (record.next()) {
                if (detailed) {
                    WordDetailModel wordDetailModel = new WordDetailModel();
                    wordDetailModel.setId(record.getString("id"));
                    wordDetailModel.setWord(record.getString("word"));
                    wordDetailModel.setPhonetic(record.getString("phonetic"));
                    wordDetailModel.setDefinition(Arrays.asList(record.getString("definition").split("\n")));
                    wordDetailModel.setTranslation(Arrays.asList(record.getString("translation").split("\n")));
                    wordDetailModel.setPos(record.getString("pos"));
                    wordDetailModel.setTag(record.getString("tag"));
                    wordDetailModel.setExchange(record.getString("exchange"));
                    // wordDetailModel.setSyno(record.getString("syno"));
                    // wordDetailModel.setStar(record.getString("star"));
                    wordModel = wordDetailModel;
                } else {
                    WordModel wordModel1 = new WordModel();
                    wordModel1.setId(record.getString("id"));
                    wordModel1.setWord(record.getString("id"));
                    wordModel1.setWord(record.getString("word"));
                    wordModel1.setDefinition(Arrays.asList(record.getString("definition").split("\n")));
                    wordModel1.setTranslation(Arrays.asList(record.getString("translation").split("\n")));
                    wordModel1.setTag(record.getString("tag"));
                    wordModel = wordModel1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (record != null) {
                try {
                    record.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return wordModel;
    }


    public List<WordBaseModel> match(String word, int limit, boolean strip, boolean detailed) {
        List<WordBaseModel> result = new ArrayList<>();
        String sql;
        try {
            if (!strip) {
                sql = "SELECT * FROM dict WHERE word >= ? ORDER BY word COLLATE NOCASE LIMIT ?;";
            } else {
                sql = "SELECT * FROM dict WHERE sw >= ? ORDER BY sw, word COLLATE NOCASE LIMIT ?;";
                word = stripWord(word);
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setInt(2, limit);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                WordBaseModel wordModel;
                if (detailed) {
                    // For detailed data, map the full data to WordDetailModel
                    WordDetailModel wordDetailModel = new WordDetailModel();
                    wordDetailModel.setId(resultSet.getString("id"));
                    wordDetailModel.setWord(resultSet.getString("word"));
                    wordDetailModel.setPhonetic(resultSet.getString("phonetic"));
                    wordDetailModel.setDefinition(Arrays.asList(resultSet.getString("definition").split("\n")));
                    wordDetailModel.setTranslation(Arrays.asList(resultSet.getString("translation").split("\n")));
                    wordDetailModel.setPos(resultSet.getString("pos"));
                    wordDetailModel.setTag(resultSet.getString("tag"));
                    wordDetailModel.setExchange(resultSet.getString("exchange"));
                    // Add other fields if needed
                    wordModel = wordDetailModel;
                } else {
                    // For non-detailed data, map to WordModel
                    WordModel wordModel1 = new WordModel();
                    wordModel1.setId(resultSet.getString("id"));
                    wordModel1.setWord(resultSet.getString("word"));
                    wordModel1.setDefinition(Arrays.asList(resultSet.getString("definition").split("\n")));
                    wordModel1.setTranslation(Arrays.asList(resultSet.getString("translation").split("\n")));
                    wordModel1.setTag(resultSet.getString("tag"));
                    wordModel = wordModel1;
                }

                result.add(wordModel);  // Add the WordBaseModel to result list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<WordBaseModel> queryBatch(List<Object> keys, boolean detailed) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM dict WHERE ");
        List<String> querys = new ArrayList<>();
        for (Object key : keys) {
            if (key instanceof Integer) {
                querys.add("id = ?");
            } else if (key instanceof String) {
                querys.add("word = ?");
            }
        }
        sql.append(String.join(" OR ", querys)).append(";");

        List<WordBaseModel> results = new ArrayList<>();

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql.toString());
            for (int i = 0; i < keys.size(); i++) {
                pstmt.setObject(i + 1, keys.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                WordBaseModel wordModel;
                if (detailed) {
                    WordDetailModel wordDetailModel = new WordDetailModel();
                    wordDetailModel.setId(resultSet.getString("id"));
                    wordDetailModel.setWord(resultSet.getString("word"));
                    wordDetailModel.setPhonetic(resultSet.getString("phonetic"));
                    wordDetailModel.setDefinition(Arrays.asList(resultSet.getString("definition").split("\n")));
                    wordDetailModel.setTranslation(Arrays.asList(resultSet.getString("translation").split("\n")));
                    wordDetailModel.setPos(resultSet.getString("pos"));
                    wordDetailModel.setTag(resultSet.getString("tag"));
                    wordDetailModel.setExchange(resultSet.getString("exchange"));
                    // Add other fields as needed
                    wordModel = wordDetailModel;
                } else {
                    WordModel wordModel1 = new WordModel();
                    wordModel1.setWord(resultSet.getString("word"));
                    wordModel1.setId(resultSet.getString("id"));
                    wordModel1.setDefinition(Arrays.asList(resultSet.getString("definition").split("\n")));
                    wordModel1.setTranslation(Arrays.asList(resultSet.getString("translation").split("\n")));
                    wordModel1.setTag(resultSet.getString("tag"));
                    wordModel = wordModel1;
                }

                // Match the original key type to ensure the correct result is added
                for (Object key : keys) {
                    if (key instanceof Integer && wordModel.getWord().equals(resultSet.getString("word"))) {
                        results.add(wordModel);
                    } else if (key instanceof String && wordModel.getWord().equals(resultSet.getString("word"))) {
                        results.add(wordModel);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    // 取得单词总数
    public int count() {
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet record = stmt.executeQuery("SELECT COUNT(*) FROM dict;");
            if (record.next()) {
                return record.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 删除单词
    public boolean remove(Object key, boolean commit) {
        String sql;
        if (key instanceof Integer) {
            sql = "DELETE FROM dict WHERE id = ?;";
        } else {
            sql = "DELETE FROM dict WHERE word = ?;";
        }

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setObject(1, key);
            pstmt.executeUpdate();
            if (commit) {
                this.conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String stripWord(String word) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                result.append(c);
            }
        }

        return result.toString().toLowerCase();
    }
}
