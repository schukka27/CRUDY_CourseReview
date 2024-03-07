package edu.virginia.sde.reviews;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CourseReviewScreenController {
    @FXML
    private Button addReview;

    @FXML
    private Label mainTitle;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox cardContainer;

    @FXML
    Button editReview;

    @FXML
    Button deleteReview;
    private ArrayList<Review> reviews;
    private DatabaseDriver databaseDriver;
    private AddCourseReviewService deleteService;

    @FXML
    public void initialize() throws SQLException {
        CourseReviewScreenService service = new CourseReviewScreenService();
        mainTitle.setText(CurrentCourse.getCourse().getMnemonic() + " " + CurrentCourse.getCourse().getNumber() + " Reviews");
        //System.out.println(service.getAllReviews());
        deleteService = new AddCourseReviewService();
        scrollPane.setContent(cardContainer);
        cardContainer.setSpacing(10);
        reviews = service.getAllReviews();
        populateCards();
        if (hasUserCreatedReview()){
            editReview.setVisible(true);
            deleteReview.setVisible(true);
            addReview.setVisible(false);
        }
        else {
            editReview.setVisible(false);
            deleteReview.setVisible(false);
            addReview.setVisible(true);
        }

    }
    public boolean hasUserCreatedReview() throws SQLException {
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
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM reviews WHERE courseID = ? AND user=? ");
            preparedStatement2.setInt(1, courseID);
            preparedStatement2.setString(2, CurrentUser.getUsername());
            ResultSet row2 = preparedStatement2.executeQuery();
            if (row2.next()) {
                return true;
            }
            else {
                return false;
            }

        }

        catch (Exception e) {
            System.out.println("Error connecting to database");
        }
        finally {
            databaseDriver.close();
        }
        return false;
    }
    private void populateCards(){
        try {
            cardContainer.getChildren().clear();
            for (Review review: reviews){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("courseReview-card.fxml"));
                VBox reviewCard = loader.load();

                CourseReviewCardController controller =  loader.getController();

                controller.setCardInfo(review);
                reviewCard.setVisible(true);
                cardContainer.getChildren().add(reviewCard);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAddReviewAction() {
        CourseReviewScreenService addReviewService = new CourseReviewScreenService();
        addReviewService.loadAddCourseScreen();
        Stage currentStage = (Stage) addReview.getScene().getWindow();
        currentStage.close();

    }
    public void handleEditReviewAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editReview-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Edit Your Review");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mainTitle.getScene().getWindow();
            currentStage.close();
            //exitdatabase();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleDeleteReviewAction() throws SQLException {
        //TODO
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
            deleteService.deleteReview(courseID,CurrentUser.getUsername());
            deleteService.courseAvg(courseID);
            initialize();
        }catch(SQLException e){
            e.printStackTrace();

        }


    }
    public void handleLogoutAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mainTitle.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackToCourseSearchAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseSearch-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Course Search Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mainTitle.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
