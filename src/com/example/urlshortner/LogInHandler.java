package com.example.urlshortner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class LogInHandler implements HttpHandler {
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

        User user = UserDao.getUserByUsername(username);
        String response;

        if (user != null && user.getPassword().equals(password)) {
            response = "Login successful";
            exchange.sendResponseHeaders(200, response.length());
        } else {
            response = "Invalid credentials";
            exchange.sendResponseHeaders(401, response.length());
        }

        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
