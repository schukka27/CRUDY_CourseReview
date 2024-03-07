package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditReviewScreenController {
    @FXML
    public TextArea courseReview;

    @FXML
    public ChoiceBox<Integer> courseRating;

    @FXML
    public Button submitReview, backButton;

    private DatabaseDriver databaseDriver;
    private AddCourseReviewService addService;

    public void initialize() throws SQLException {
        courseRating.getItems().addAll(1, 2, 3, 4, 5);
        addService = new AddCourseReviewService();
        databaseDriver = new DatabaseDriver("userinfo");
        Connection connection = databaseDriver.connect();
        String username = CurrentUser.getUsername();
        int courseID = 0;
        try {
            String mnemonic = CurrentCourse.getCourse().getMnemonic();
            String courseName = CurrentCourse.getCourse().getTitle();
            int courseNum = CurrentCourse.getCourse().getNumber();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic = ? AND courseName=? AND courseNumber=?");
            preparedStatement.setString(1, mnemonic);
            preparedStatement.setString(2, courseName);
            preparedStatement.setInt(3, courseNum);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                courseID = row.getInt("ID");
            }
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ? AND user=?");
            preparedStatement1.setInt(1, courseID);
            preparedStatement1.setString(2, username);
            ResultSet row1 = preparedStatement1.executeQuery();
            while (row1.next()) {
                courseReview.setText(row1.getString("reviewComment"));
                courseRating.setValue(row1.getInt("courseRating"));
            }
            databaseDriver.close();

        } catch (SQLException error){
            System.out.println("Error connecting to the database");
        }
    }
    public void handleSubmitReviewAction() throws SQLException {
        //initialize();
        databaseDriver = new DatabaseDriver("userinfo");
        Connection connection = databaseDriver.connect();
        int courseID = 0;
        try {
            String mnemonic = CurrentCourse.getCourse().getMnemonic();
            String courseName = CurrentCourse.getCourse().getTitle();
            int courseNum = CurrentCourse.getCourse().getNumber();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic = ? AND courseName=? AND courseNumber=?");
            preparedStatement.setString(1, mnemonic);
            preparedStatement.setString(2, courseName);
            preparedStatement.setInt(3, courseNum);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                courseID = row.getInt("ID");
            }
            databaseDriver.close();
            addService.editReview(courseID,CurrentUser.getUsername(),courseRating.getValue(),courseReview.getText());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseReview-screen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Course Reviews");
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) courseReview.getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }catch(SQLException e){
            e.printStackTrace();

        }

    }
    public void handleBackToCourseReviewScreen() {
        AddCourseReviewService addCourseReviewService = new AddCourseReviewService();
        addCourseReviewService.loadCourseReviewScreen();
        Stage currentStage = (Stage) courseReview.getScene().getWindow();
        currentStage.close();
    }
}
