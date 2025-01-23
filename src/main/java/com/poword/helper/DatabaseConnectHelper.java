package com.poword.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectHelper {
    private static DatabaseConnectHelper instance;
    private Connection conn;
    private String dbname;

    private DatabaseConnectHelper(String dbname) {
        this.dbname = dbname;
    }

    public static DatabaseConnectHelper getInstance(String dbname) {
        if (instance == null) {
            synchronized (DatabaseConnectHelper.class) {
                if (instance == null) {
                    instance = new DatabaseConnectHelper(dbname);
                    instance.open();
                }
            }
        }
        return instance;
    }

    // 打开数据库连接
    public void open() {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
                System.out.println("Connection to SQLite has been established.");
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 关闭数据库连接
    public void close() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
                System.out.println("Connection to SQLite has been closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public Connection getConnection() {
        return this.conn;
    }
}
