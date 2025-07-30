package com.example.urlshortner;

import com.sun.net.httpserver.*;
//import com.sun.tools.javac.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrlShortenerServer {
    static final Map<String, String> urlMap = new HashMap<>();

    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/shorten", new ShortenHandler());
        server.createContext("/redirect", new RedirectHandler());
        server.createContext("/signup", new SignUpHandler());
        server.createContext("/login", new LogInHandler());
        server.createContext("/", new RootHandler());
        server.setExecutor(null);
        System.out.println("Server started on port 8100...");
        server.start();
    }

    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Welcome to URL Shortener!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class ShortenHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            String requestBody = body.toString();
            String[] parts = requestBody.split("=");
            String originalUrl = parts.length == 2 ? parts[1] : null;

            if (originalUrl == null || originalUrl.isEmpty()) {
                String msg = "Missing 'url' parameter in body.";
                exchange.sendResponseHeaders(400, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes());
                }
                return;
            }

            String shortCode = UUID.randomUUID().toString().substring(0, 6);
            urlMap.put(shortCode, originalUrl);
            String shortened = "http://localhost:8000/redirect/" + shortCode;
            exchange.sendResponseHeaders(200, shortened.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(shortened.getBytes());
            }
        }
    }

    static class RedirectHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.getPath();
            String[] parts = path.split("/");

            if (parts.length != 3) {
                exchange.sendResponseHeaders(400, -1);
                return;
            }

            String code = parts[2];
            String originalUrl = urlMap.get(code);

            if (originalUrl == null) {
                String msg = "Short URL not found!";
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes());
                }
                return;
            }

            exchange.getResponseHeaders().set("Location", originalUrl);
            exchange.sendResponseHeaders(302, -1);
        }
    }
}
