package com.example.urlshortner;

import java.util.HashMap;

public class UserData {
    private static HashMap<String, User> userMap = new HashMap<>();

    public  boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    public  void saveUser(User user) {
        userMap.put(user.getUsername(), user);
        System.out.println(userMap);
    }

    public static  User getUser(String username) {
        return userMap.get(username);
    }
}

