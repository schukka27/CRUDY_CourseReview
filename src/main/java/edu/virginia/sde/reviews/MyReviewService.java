package edu.virginia.sde.reviews;

import java.sql.*;
import java.util.ArrayList;

public class MyReviewService {
    private DatabaseDriver databaseDriver;

    public ArrayList<Review> getAllMyReviews(){
        ArrayList<Review> output = new ArrayList<>();
        String currentUsername = CurrentUser.getUsername();
        int courseID = 0;
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reviews WHERE user = ?");
            preparedStatement.setString(1, currentUsername);
            ResultSet row = preparedStatement.executeQuery();
            while(row.next()){
                User user = new User(CurrentUser.getUsername());
                courseID = row.getInt("courseID");
                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM courses WHERE ID = ?");
                preparedStatement2.setInt(1, courseID);
                ResultSet row2 = preparedStatement2.executeQuery();
                if (row2.next()) {
                    Course course = new Course(row2.getString("mnemonic"),row2.getInt("courseNumber"), row2.getString("courseName"), row2.getInt("courseRating") );
                    Review review = new Review(course,row.getInt("courseRating"), Timestamp.valueOf(row.getString("timestamp")),row.getString("reviewComment"),user );
                    output.add(review);
                }


            }
        } catch (SQLException error){
            System.out.println("fourth one");

            System.out.println("Error parsing courses");
        }
        finally {
            databaseDriver.close();
        }
        return output;
    }
}
