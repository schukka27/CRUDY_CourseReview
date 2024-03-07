package edu.virginia.sde.reviews;

public class CurrentUser {
    private static String username; // asked how to make variables that can be used

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String newUsername) {
        CurrentUser.username = newUsername;
    }
}
