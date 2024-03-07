package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MyReviewCardController {
    @FXML
    private Label ratingText, commentText, courseText;

    private Review review;

    public void setCardInfo(Review review){
        this.review = review;
        ratingText.setText("Rating: " + review.getRating());
        if (!review.getComment().isEmpty()){
            commentText.setText(review.getComment());
        }
        Course course = review.getCourse();
        courseText.setText("See All Reviews for " + course.getMnemonic() + " " + course.getNumber());
    }
    public void seeAllReviewsForCourse(){
        CurrentCourse.setCourse(this.review.getCourse());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseReview-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Course Review Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) ratingText.getScene().getWindow();
            currentStage.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
