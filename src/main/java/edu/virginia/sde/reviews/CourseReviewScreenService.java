package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CourseReviewScreenService {
    private DatabaseDriver databaseDriver;

    public void loadAddCourseScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addReview-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Add a new review");
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Review> getAllReviews(){
        ArrayList<Review> output = new ArrayList<>();
        int currentCourseNumber = CurrentCourse.getCourse().getNumber();
        String currentCourseMnemonic = CurrentCourse.getCourse().getMnemonic();
        String currentCourseName = CurrentCourse.getCourse().getTitle();
        int courseIDFromQuery;
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement prepared1 = connection.prepareStatement("SELECT ID FROM courses WHERE mnemonic = ? AND courseNumber = ? AND courseName = ?");
            prepared1.setString(1, currentCourseMnemonic);
            prepared1.setInt(2, currentCourseNumber);
            prepared1.setString(3, currentCourseName);
            ResultSet resultSet = prepared1.executeQuery();
            if (resultSet.next()) {
                courseIDFromQuery = resultSet.getInt("ID");
            } else {
                System.out.println("No matching row found.");
                courseIDFromQuery = 0; //placeholder
            }
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ?");
            preparedStatement.setInt(1, courseIDFromQuery);
            ResultSet row = preparedStatement.executeQuery();
            while(row.next()){
                User user = new User(CurrentUser.getUsername());
                Review review = new Review(CurrentCourse.getCourse(),row.getInt("courseRating"),Timestamp.valueOf(row.getString("timestamp")),row.getString("reviewComment"),user );
                output.add(review);
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
