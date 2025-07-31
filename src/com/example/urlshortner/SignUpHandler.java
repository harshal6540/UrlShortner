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

    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

    
    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(204, -1); 
        return;
    }

    
    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        exchange.sendResponseHeaders(405, -1); 
        return;
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
    String line = reader.readLine();
    String[] parts = line.split("&");

    String username = "";
    String password = "";

    for (String part : parts) {
        String[] pair = part.split("=");
        if (pair.length == 2) {
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
            if (key.equals("username")) username = value;
            else if (key.equals("password")) password = value;
        }
    }

    String response;
    User existingUser = UserDao.getUserByUsername(username);
    if (existingUser != null) {
        response = "Username already exists";
        exchange.sendResponseHeaders(409, response.getBytes().length);
    } else {
        User newUser = new User(username, password);
        UserDao.saveUser(newUser);
        response = "Signup successful for user: " + username;
        exchange.sendResponseHeaders(200, response.getBytes().length);
    }

    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
}




}