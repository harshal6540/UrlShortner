package com.example.urlshortner;

import java.sql.*;

public class UserDao {

    public static void saveUser(User user) {
        String sql = "INSERT INTO t_users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            System.out.println("User saved to DB: " + user.getUsername());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM t_users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
