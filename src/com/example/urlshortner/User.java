package com.example.urlshortner;

public class User {
    private static String username;
    private static String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
