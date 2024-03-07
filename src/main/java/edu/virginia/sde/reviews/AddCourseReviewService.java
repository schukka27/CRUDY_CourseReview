package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AddCourseReviewService {
    private DatabaseDriver databaseDriver;
    public void loadCourseReviewScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseReview-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Course Review Screen");
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean addReview(int courseID,String username, int courseRating, String content){
        try{
            if (courseID == 0 ||  username.equals("") || courseRating == 0){
                return false;
            }
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ? AND user = ? ");
            preparedStatementInitial.setInt(1,courseID);
            preparedStatementInitial.setString(2,username);
            var row = preparedStatementInitial.executeQuery();
            if (row.next()) {
                return false;
            }
            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reviews (timestamp,courseID,user,courseRating,reviewComment) VALUES (?,?, ?, ?, ?)");
            preparedStatement.setString(1,timestamp1.toString());
            preparedStatement.setInt(2, courseID);
            preparedStatement.setString(3, username);
            preparedStatement.setInt(4, courseRating);
            preparedStatement.setString(5, content);
            preparedStatement.executeUpdate();
            databaseDriver.close();
            courseAvg(courseID);
        } catch (SQLException error){
            System.out.println("Error adding a review");

        }
        return true;
    }

    public boolean editReview(int courseID,String username, int courseRating, String content){ // should only display if user has already created rating for a course
        try{
            if (courseID == 0 ||  username.equals("") || courseRating == 0){
                return false;
            }
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
//            PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ? AND user = ? ");
//            preparedStatementInitial.setInt(1,courseID);
//            preparedStatementInitial.setString(2,username);
//            var row = preparedStatementInitial.executeQuery();
//            if (row.next()) {
//                return false;
//            }
            Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reviews SET courseRating = ?, reviewComment = ?, timestamp = ? WHERE user = ? and courseID = ? ");
            preparedStatement.setInt(1, courseRating);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3,timestamp1.toString());
            preparedStatement.setString(4, username);
            preparedStatement.setInt(5, courseID);
            preparedStatement.executeUpdate();
            databaseDriver.close();
            courseAvg(courseID);
        } catch (SQLException error){
            System.out.println("Error updating a review ");

        }
        return true;
    }
    public void deleteReview(int courseID,String username){ // should only display if user has already created rating for a course
        try{
//            if (courseID == 0 ||  username.equals("") || courseRating == 0){
//                return;
//            }
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
//            PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ? AND user = ? ");
//            preparedStatementInitial.setInt(1,courseID);
//            preparedStatementInitial.setString(2,username);
//            var row = preparedStatementInitial.executeQuery();
//            if (row.next()) {
//                return;
//            }
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reviews WHERE user = ? and courseID = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, courseID);
            preparedStatement.executeUpdate();
            databaseDriver.close();
            courseAvg(courseID);
        } catch (SQLException error){
            System.out.println("Error deleting a review");

        }
    }
    public void courseAvg(int courseID) throws SQLException {
        if (courseID == 0){
            return;
        }
        databaseDriver = new DatabaseDriver("userinfo");
        Connection connection = databaseDriver.connect();
        PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ?");
        preparedStatementInitial.setInt(1, courseID);
        double sum = 0;
        double increment = 0;
        var row = preparedStatementInitial.executeQuery();
        while (row.next()) {
            sum += row.getInt("courseRating");
            increment++;
        }
        if (sum != 0 && increment != 0){
            double average = sum/increment;
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE courses SET courseRating = ? WHERE ID = ?");
            preparedStatement.setDouble(1, average);
            preparedStatement.setInt(2, courseID);
            preparedStatement.executeUpdate();
        } else{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE courses SET courseRating = ? WHERE ID = ?");
            preparedStatement.setDouble(1, 0.0);
            preparedStatement.setInt(2, courseID);
            preparedStatement.executeUpdate();
        }

        databaseDriver.close();

    }

}
