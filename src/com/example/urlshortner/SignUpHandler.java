package com.example.urlshortner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SignUpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        UserData userData = new UserData();
        
        if (!exchange.getRequestMethod().equals("POST")) {
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
        String response = "";

        if(userData.userExists(username)) {
            response = "Username already exists";
            exchange.sendResponseHeaders(409, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
            return;
        }
        else{
            User user = new User(username, password);
            userData.saveUser(user);
            response ="Signup successful" + " for user: " + username;
        }
        

        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
