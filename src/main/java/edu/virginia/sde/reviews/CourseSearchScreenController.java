package edu.virginia.sde.reviews;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class CourseSearchScreenController {
@FXML
private TextField mnemonicInput;
@FXML
private TextField numberInput;
@FXML
private TextField titleInput;

@FXML
private Label messageLabel;

@FXML
private VBox cardContainer;

@FXML
private TextField mnemonicAdd;

@FXML
private TextField numberAdd;

@FXML
private TextField titleAdd;
private ArrayList<Course> value;
private CourseSearchService courseSearchService;

@FXML
private ScrollPane scrollPane;

@FXML
private Button myReviews;

public void initialize() throws SQLException {
    try{
        courseSearchService = new CourseSearchService();
        scrollPane.setContent(cardContainer);
        cardContainer.setSpacing(10);
        value = courseSearchService.getAllCourses();
        populateCards();
    } catch (SQLException error){
        System.out.println("error connecting to Course Search Service");
    }
}

    public void handleAddCourseAction() throws SQLException {
    initialize();
    messageLabel.setVisible(false);
    String addedMnemonic = mnemonicAdd.getText();
    String addedCourseNumber = numberAdd.getText();
    String addedTitle = titleAdd.getText();
    boolean mnemonic = true;
    boolean courseNum = true;
    boolean title = true;
    StringBuilder errorMessages = new StringBuilder();// got idea for flexible error messages from chatgpt: how to create error message based on multiple boolean conditions where multiple conditions can be false
    errorMessages.append("Please input valid")  ;
    try{
        if (!addedMnemonic.equals("") || !addedCourseNumber.equals("") || !addedTitle.equals("")){
            int coursenumber = Integer.parseInt(addedCourseNumber);
            if (addedTitle.isEmpty()) {
                throw new NumberFormatException();
            }
            if (2 > addedMnemonic.length()|| addedMnemonic.length() > 4 || addedMnemonic.isEmpty() ||!addedMnemonic.matches("[a-zA-Z]+")){ // asked chatgpt: how to check if only letter inputs are in an input
                mnemonic = false;
                errorMessages.append(" mnemonic (between 2 and 4 letters);");
                messageLabel.setVisible(true);
                messageLabel.setText(errorMessages.toString());
            }
            if (coursenumber < 1000 || coursenumber > 9999){
                courseNum = false;
                errorMessages.append(" course number (4 digits);");
                messageLabel.setVisible(true);
                messageLabel.setText(errorMessages.toString());
            }
            if (addedTitle.length() > 50){
                title = false;
                errorMessages.append(" title (between 1 and 50 characters)");
                messageLabel.setVisible(true);
                messageLabel.setText(errorMessages.toString());
            }
            if (mnemonic == true && courseNum == true && title == true){
                boolean unique = courseSearchService.addCourse(addedMnemonic.trim().toUpperCase(),coursenumber,addedTitle.trim());
                if (unique == false){
                    messageLabel.setVisible(true);
                    messageLabel.setText("Please input unique course");
                    return;
                }
                mnemonicInput.setText("");
                numberInput.setText("");
                titleInput.setText("");
                value = courseSearchService.getAllCourses();
                this.populateCards();
                mnemonicAdd.setText("");
                numberAdd.setText("");
                titleAdd.setText("");

            }
            else{
                messageLabel.setVisible(true);
                messageLabel.setText(errorMessages.toString());
                mnemonicAdd.setText("");
                numberAdd.setText("");
                titleAdd.setText("");
            }

        }
        else {
            messageLabel.setVisible(true);
            messageLabel.setText("Please input valid mnemonic, number, and title");
        }
    } catch (NumberFormatException error){
        messageLabel.setVisible(true);
        messageLabel.setText("Please fill out all the fields");
        mnemonicAdd.setText("");
        numberAdd.setText("");
        titleAdd.setText("");
        }
    }
    public void handleSearchAction() throws SQLException {
        initialize();
        if (mnemonicInput.getText().equals("") && numberInput.getText().equals("") && titleInput.getText().equals("")){
            value = courseSearchService.getAllCourses();
        }
        else if (!mnemonicInput.getText().equals("") && numberInput.getText().equals("") && titleInput.getText().equals("")){
            String mnemonicText = mnemonicInput.getText();
            value = courseSearchService.getCourseDepartment(mnemonicText.trim());
        } else if (mnemonicInput.getText().equals("") && !numberInput.getText().equals("") && titleInput.getText().equals("")){
            String coursenumberText = numberInput.getText();
            try{
                int castedCourseNumber = Integer.parseInt(coursenumberText);
                value = courseSearchService.getCourseNumber(castedCourseNumber);
            } catch(NumberFormatException error){
                messageLabel.setText("Please input a valid course number");
                return;
            }

        } else if (mnemonicInput.getText().equals("") && numberInput.getText().equals("") && !titleInput.getText().equals("")){
            String coursenameText = titleInput.getText();
            value = courseSearchService.getCourseName(coursenameText.trim());
        } else if (!mnemonicInput.getText().equals("") && !numberInput.getText().equals("") && !titleInput.getText().equals("")){
            String mnemonicInputText = mnemonicInput.getText();
            String coursenumberText = numberInput.getText();
            try{
                int castedCourseNumber = Integer.parseInt(coursenumberText);
                String coursenameText = titleInput.getText();
                value = courseSearchService.getCourse(mnemonicInputText.trim(),castedCourseNumber,coursenameText.trim());
            } catch(NumberFormatException error){
                messageLabel.setText("Please input a valid course number");
                return;
            }
        } else if (!mnemonicInput.getText().equals("") && !numberInput.getText().equals("") && titleInput.getText().equals("")){
            String mnemonicInputText = mnemonicInput.getText();
            String coursenumberText = numberInput.getText();
            try{
                int castedCourseNumber = Integer.parseInt(coursenumberText);
                value = courseSearchService.getMnemonicCourseNum(mnemonicInputText.trim(),castedCourseNumber);
            } catch(NumberFormatException error){
                messageLabel.setText("Please input a valid course number");
                return;
            }
        } else if (mnemonicInput.getText().equals("") && !numberInput.getText().equals("") && !titleInput.getText().equals("")){
            String coursenumberText = numberInput.getText();
            try{
                int castedCourseNumber = Integer.parseInt(coursenumberText);
                String coursenameText = titleInput.getText();
                value = courseSearchService.getCourseNumCourseName(castedCourseNumber, coursenameText.trim());
            } catch(NumberFormatException error){
                messageLabel.setText("Please input a valid course number");
                return;
            }
        } else if (!mnemonicInput.getText().equals("") && numberInput.getText().equals("") && !titleInput.getText().equals("")){
            String mnemonicInputText = mnemonicInput.getText();
            String coursenameText = titleInput.getText();
            value = courseSearchService.getMnemonicandName(mnemonicInputText.trim(), coursenameText.trim());
        }
        this.populateCards();
    }
    private void populateCards() {
        try {
            cardContainer.getChildren().clear();
            for (Course course: value){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("course-card.fxml"));
                VBox courseCard = loader.load();

                CourseCardController controller =  loader.getController();

                controller.setCardInfo(course);
                courseCard.setVisible(true);
                cardContainer.getChildren().add(courseCard);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleMyReviewsAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("myReviews-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("My Reviews");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mnemonicInput.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleLogoutAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) mnemonicInput.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



}
