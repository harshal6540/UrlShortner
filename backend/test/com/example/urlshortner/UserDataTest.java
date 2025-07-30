package com.example.urlshortner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDataTest {

    private UserData userData;

    @BeforeEach
    public void setup() {
        userData = new UserData();
    }

    @Test
    public void testUserDoesNotExistInitially() {
        assertFalse(userData.userExists("harshal"));
    }

    @Test
    public void testSaveUserAndCheckExistence() {
        User user = new User("harshal", "password123");
        userData.saveUser(user);
        assertTrue(userData.userExists("harshal"));
    }

    @Test
    public void testGetUser() {
        User user = new User("harshal", "password123");
        userData.saveUser(user);

        User fetchedUser = userData.getUser("harshal");
        assertNotNull(fetchedUser);
        assertEquals("harshal", fetchedUser.getUsername());
        assertEquals("password123", fetchedUser.getPassword());
    }

    @Test
    public void testOverwriteExistingUser() {
        User user1 = new User("harshal", "password123");
        userData.saveUser(user1);

        User user2 = new User("harshal", "newpassword456");
        userData.saveUser(user2);

        User fetchedUser = userData.getUser("harshal");
        assertEquals("newpassword456", fetchedUser.getPassword());
    }
}
