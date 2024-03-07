package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyReviewsScreenController {

    @FXML
    private Button returnToLoginButton1;

    @FXML
    private Label myReviewsLabel;

    @FXML
    private Button returnToCourseSearch;

    @FXML
    private VBox cardContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox courseComponent;

    @FXML
    private Label mnemonic;

    @FXML
    private Label numberLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label reviewsLabel;

    @FXML
    private Label userRatingLabel;

    @FXML
    private Label numberValueLabel;

    @FXML
    private TextArea commentTextArea;

    private ArrayList<Review> reviews;

    public void initialize() {
        MyReviewService service = new MyReviewService();
        scrollPane.setContent(cardContainer);
        cardContainer.setSpacing(10);
        reviews = service.getAllMyReviews();
        populateCards();
    }

    public void handleGoToLoginScreenButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) returnToLoginButton1.getScene().getWindow();
            currentStage.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleBackButtonAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseSearch-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Course Search");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) returnToCourseSearch.getScene().getWindow();
            currentStage.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void populateCards(){
        try {
            cardContainer.getChildren().clear();
            for (Review review: reviews){
                CurrentCourse.setCourse(review.getCourse());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("myReview-card.fxml"));
                VBox courseCard = loader.load();

                MyReviewCardController controller =  loader.getController();

                controller.setCardInfo(review);
                courseCard.setVisible(true);
                cardContainer.getChildren().add(courseCard);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

