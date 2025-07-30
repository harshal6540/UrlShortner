package com.example.urlshortner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class UrlShortenerServerTest {

    @Test
    public void testUrlMapPutAndGet() {
        String originalUrl = "https://example.com";
        String shortCode = "abc123";

        
        UrlShortenerServer.urlMap.put(shortCode, originalUrl);

      
        String fetchedUrl = UrlShortenerServer.urlMap.get(shortCode);

        
        assertEquals(originalUrl, fetchedUrl);
    }

    @Test
    public void testUrlMapReturnsNullForInvalidCode() {
        String result = UrlShortenerServer.urlMap.get("invalid");
        assertNull(result);
    }

    @Test
    public void testShortCodeLength() {
        String code = java.util.UUID.randomUUID().toString().substring(0, 6);
        assertEquals(6, code.length());
    }
}
