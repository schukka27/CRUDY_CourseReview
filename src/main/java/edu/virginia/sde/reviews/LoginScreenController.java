package edu.virginia.sde.reviews;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginScreenController {

    @FXML
    private TextField usernameInput; // asked chatgpt how to access parameters in fxml files
    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label errorMessageLabel;

    private DatabaseDriver databaseDriver;


    @FXML
    public boolean checkUserInfo() throws SQLException {
        databaseDriver = new DatabaseDriver("userinfo");
        Connection connection = databaseDriver.connect();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM credentials WHERE username = ?");
            preparedStatement.setString(1,username);
            ResultSet row = preparedStatement.executeQuery(); // asked chatgpt how to access values in outputted row
            if (row.next()){
                if (password.equals(row.getString("password"))){
                    return true;

                }
            } else{
                return false;
            }
        } catch (SQLException error){
            System.out.println("Error parsing the database" + error.getMessage());
        }
        finally { // asked chatgpt how to make sure connection is closed no matter what
            databaseDriver.close();
        }

        return false;
    }
    @FXML
    public void exitdatabase(){
        if (databaseDriver != null){
            databaseDriver.close();
        }
    }

    public void handleLoginAction() throws SQLException {
        if (checkUserInfo() == true){
            CurrentUser.setUsername(usernameInput.getText());
            errorMessageLabel.setText("Successfully logged in");
            //start of testing: anything below this until the next comment saying "end of testing" should be deleted
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("courseSearch-screen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Course Search Screen");
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) usernameInput.getScene().getWindow();
                currentStage.close();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            //end of testing.
        }
        else{
            errorMessageLabel.setText("Incorrect login information. Please try again.");
        }
        errorMessageLabel.setVisible(true);
    }

    public void handleCreateNewUserAction() {
        errorMessageLabel.setText("You pressed the button!");
        errorMessageLabel.setVisible(true);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Signup Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) errorMessageLabel.getScene().getWindow();
            currentStage.close();
            //exitdatabase();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeApplication() {
        //exitdatabase();
        Platform.exit();
    }
}
