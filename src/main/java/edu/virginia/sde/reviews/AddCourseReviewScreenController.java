package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AddCourseReviewScreenController {
    @FXML
    private TextArea courseReview;

    @FXML
    private Label integerErrorLabel;

    @FXML
    private Label courseMnemonicLabel;
    @FXML
    private Label courseNumberLabel;

    @FXML
    private Label courseTitleLabel;

    @FXML
    public ChoiceBox<Integer> courseRating;
    private String courseMnemonic;
    private int courseNumber;
    private String courseTitle;
    private DatabaseDriver databaseDriver;
    private AddCourseReviewService addService;
    @FXML
    private Label ratingErrorLabel;

    @FXML
    public void initialize() {
        courseRating.getItems().addAll(1,2,3,4,5);
        addService = new AddCourseReviewService();
    }
    public void handleSubmitReviewAction() throws SQLException {
        if (courseRating.getValue() == null) {
            ratingErrorLabel.setVisible(true);
            return;
        }
        initialize();
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
            addService.addReview(courseID,CurrentUser.getUsername(),courseRating.getValue(),courseReview.getText());
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
        finally {
            databaseDriver.close();
        }


    }
    public void setCourseData(String courseMnemonic, int courseNumber, String courseTitle) {
        courseMnemonicLabel.setText(courseMnemonic);
        courseNumberLabel.setText(Integer.toString(courseNumber));
        courseTitleLabel.setText(courseTitle);
        this.courseMnemonic = courseMnemonic;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
    }

    public void handleBackToCourseReviewScreen() {
        AddCourseReviewService addCourseReviewService = new AddCourseReviewService();
        addCourseReviewService.loadCourseReviewScreen();
        Stage currentStage = (Stage) integerErrorLabel.getScene().getWindow();
        currentStage.close();
    }
}
