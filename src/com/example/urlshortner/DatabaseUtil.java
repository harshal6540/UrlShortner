package com.example.urlshortner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseUtil {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    public static void initializeSchema() {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         BufferedReader reader = new BufferedReader(new FileReader("Schema.sql"))) {

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        stmt.execute(sb.toString());
        System.out.println("Database schema initialized successfully!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    static {
        try {
            Properties props = new Properties();
        try (InputStream input = new FileInputStream("connection.properties")) {
            props.load(input);
        }

            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");

            

            Class.forName("org.h2.Driver");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    
}
