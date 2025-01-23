package com.poword.dao;
import java.sql.*;
import java.util.*;
import org.json.JSONObject;

import com.poword.model.WordBaseModel;
import com.poword.model.WordDetailModel;
import com.poword.model.WordModel;
import com.poword.helper.DatabaseConnectHelper;

public class StarDictDao {

    private Connection conn;
    private boolean verbose;
    private List<Map.Entry<String, Integer>> fields;


    public StarDictDao() {
        this.conn = DatabaseConnectHelper.getInstance("src\\main\\java\\com\\poword\\resources\\db\\stardict.db").getConnection();
    }

    // 数据库记录转化为字典
    private Map<String, Object> recordToObj(ResultSet record) throws SQLException {
        if (record == null) {
            return null;
        }
        Map<String, Object> word = new HashMap<>();
        for (Map.Entry<String, Integer> field : fields) {
            word.put(field.getKey(), record.getObject(field.getValue()));
        }
        String detail = (String) word.get("detail");
        if (detail != null) {
            try {
                JSONObject obj = new JSONObject(detail);
                word.put("detail", obj.toMap());
            } catch (Exception e) {
                word.put("detail", null);
            }
        }
        return word;
    }

    // 关闭数据库
    public void close() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.conn = null;
    }

    // 输出日志
    private void out(String text) {
        if (this.verbose) {
            System.out.println(text);
        }
    }

        /**
     * @param word
     * @return
     */
    public int getIdByWord(String word){
        String sql = "SELECT id FROM stardict WHERE word = ?";
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
                sql = "SELECT * FROM stardict WHERE id = ?;";
            } else if (key instanceof String) {
                sql = "SELECT * FROM stardict WHERE word = ?;";
            } else {
                return null;
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setObject(1, key);
            record = pstmt.executeQuery();

            if (record.next()) {
                if (detailed) {
                    WordDetailModel wordDetailModel = new WordDetailModel();
                    wordDetailModel.setWord(record.getString("id"));
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


    // 查询单词匹配
    public List<Map.Entry<Integer, String>> match(String word, int limit, boolean strip) {
        List<Map.Entry<Integer, String>> result = new ArrayList<>();
        String sql;
        try {
            if (!strip) {
                sql = "SELECT id, word FROM stardict WHERE word >= ? ORDER BY word COLLATE NOCASE LIMIT ?;";
            } else {
                sql = "SELECT id, word FROM stardict WHERE sw >= ? ORDER BY sw, word COLLATE NOCASE LIMIT ?;";
                word = stripWord(word);
            }
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setInt(2, limit);
            ResultSet records = pstmt.executeQuery();
            while (records.next()) {
                result.add(new AbstractMap.SimpleEntry<>(records.getInt("id"), records.getString("word")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 批量查询
    public List<Map<String, Object>> queryBatch(List<Object> keys) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM stardict WHERE ");
        List<String> querys = new ArrayList<>();
        for (Object key : keys) {
            if (key instanceof Integer) {
                querys.add("id = ?");
            } else if (key instanceof String) {
                querys.add("word = ?");
            }
        }
        sql.append(String.join(" OR ", querys)).append(";");
        Map<String, Map<String, Object>> queryWord = new HashMap<>();
        Map<Integer, Map<String, Object>> queryId = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql.toString());
            for (int i = 0; i < keys.size(); i++) {
                pstmt.setObject(i + 1, keys.get(i));
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> obj = recordToObj(resultSet);
                queryWord.put(((String) obj.get("word")).toLowerCase(), obj);
                queryId.put((Integer) obj.get("id"), obj);
            }

            for (Object key : keys) {
                if (key instanceof Integer) {
                    results.add(queryId.getOrDefault(key, null));
                } else if (key instanceof String) {
                    results.add(queryWord.getOrDefault(((String) key).toLowerCase(), null));
                } else {
                    results.add(null);
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
            ResultSet record = stmt.executeQuery("SELECT COUNT(*) FROM stardict;");
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
            sql = "DELETE FROM stardict WHERE id = ?;";
        } else {
            sql = "DELETE FROM stardict WHERE word = ?;";
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

    // 提交变更
    public boolean commit() {
        try {
            this.conn.commit();
        } catch (SQLException e) {
            try {
                this.conn.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
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
