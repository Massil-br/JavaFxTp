package com.massil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:tickets.db";

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS support_tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "customer_name TEXT NOT NULL," +
                "priority TEXT NOT NULL," +
                "created_at TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "urgent INTEGER NOT NULL," +
                "status TEXT NOT NULL" +
                ")";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
