package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CourseReviewCardController {
    @FXML
    private Label ratingText, commentText, timeStamp;

    public void setCardInfo(Review review){
        ratingText.setText(String.valueOf(review.getRating()));
        if (!review.getComment().isEmpty()){
            commentText.setText(review.getComment());
        }
        timeStamp.setText(String.valueOf(review.getTimestamp()));
    }
}
