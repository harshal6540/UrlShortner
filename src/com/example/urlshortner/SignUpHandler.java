package com.example.urlshortner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SignUpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String line = reader.readLine();
        String[] parts = line.split("&");

        String username = "";
        String password = "";

        for (String part : parts) {
            String[] pair = part.split("=");
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
            if (key.equals("username")) username = value;
            else if (key.equals("password")) password = value;
        }

        String response;
        User existingUser = UserDao.getUserByUsername(username);
        if (existingUser != null) {
            response = "Username already exists";
            exchange.sendResponseHeaders(409, response.length());
        } else {
            User newUser = new User(username, password);
            UserDao.saveUser(newUser);
            response = "Signup successful for user: " + username;
            exchange.sendResponseHeaders(200, response.length());
        }

        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
