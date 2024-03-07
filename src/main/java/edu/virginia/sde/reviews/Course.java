package edu.virginia.sde.reviews;

import java.util.ArrayList;

public class Course {
    private String mnemonic;
    private int number;
    private String title;
    private double avgRating;

    public Course(String mnemonic, int number, String title, double averageRating) {
        this.mnemonic = mnemonic;
        this.number = number;
        this.title = title;
        this.avgRating =  averageRating;
    }

    public String getMnemonic() {
        return mnemonic;
    }


    public int getNumber() {
        return number;
    }


    public String getTitle() {
        return title;
    }


    public double getAvgRating() {
        return avgRating;
    }
}
