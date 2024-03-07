package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseCardController {
    @FXML
    private Label mnemonic, number, title, avgRating;

    private Course course;

    public void setCardInfo(Course course){
        this.course = course;
        mnemonic.setText(course.getMnemonic());
        number.setText(String.valueOf(course.getNumber()));
        title.setText(course.getTitle());
        if (course.getAvgRating() == 0.0){
            avgRating.setText("-");
        }else{
            avgRating.setText(String.format("%.2f",course.getAvgRating()));
        }
    }
    public void handleGoToReviewAction (){
        CurrentCourse.setCourse(this.course);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseReview-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Course Review Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mnemonic.getScene().getWindow();
            currentStage.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
