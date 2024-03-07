package edu.virginia.sde.reviews;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<Review> reviews;

    public String getUsername() {
        return username;
    }
    public User(String username){
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
