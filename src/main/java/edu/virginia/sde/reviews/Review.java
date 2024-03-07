package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    private Course course;
    private double rating;
    private Timestamp timestamp;
    private String comment;
    private User user;


    public Review(Course course, double rating, Timestamp timestamp, String comment, User user) {
        this.course = course;
        this.rating = rating;
        this.timestamp = timestamp;
        this.comment = comment;
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public double getRating() {
        return rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getComment() {
        return comment;
    }


}
