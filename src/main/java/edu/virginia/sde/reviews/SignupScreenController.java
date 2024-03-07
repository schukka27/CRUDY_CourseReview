package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SignupScreenController {
    @FXML
    private TextField newUsernameInput; // asked chatgpt how to access parameters in fxml files
    @FXML
    private TextField newPasswordInput;

    private DatabaseDriver databaseDriver;


    @FXML
    private Label messageLabel;
    @FXML

    public void handleCreateNewUserAction() throws SQLException {
        addUsers();
    }

    @FXML
    public void connectdatabase() throws SQLException { // check if I need this

    }
    @FXML
    public void addUsers() throws SQLException {
        databaseDriver = new DatabaseDriver("userinfo");
        Connection connection = databaseDriver.connect();
        String username = newUsernameInput.getText();
        String password = newPasswordInput.getText();
        messageLabel.setTextFill(Paint.valueOf("#fc3535"));
        if (username.isEmpty()) {
            messageLabel.setText("No username provided.");
            messageLabel.setVisible(true);
            return;
        }
        else if (password.length() < 8){
            messageLabel.setText("Password must be at least 8 characters.");
            messageLabel.setVisible(true);
            return;
        }
        try {
            PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM credentials WHERE username = ?");
            preparedStatementInitial.setString(1, username);
            var row = preparedStatementInitial.executeQuery();
            if (row.next()) {
                messageLabel.setText("Username already taken. Please try again.");
                messageLabel.setVisible(true);
                return;
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO credentials (username, password) VALUES (?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            messageLabel.setText("User added successfully!");
            messageLabel.setTextFill(Paint.valueOf("#02864a"));
            messageLabel.setVisible(true);
        } catch (SQLException error) {
            System.out.println("Error adding to the database: " + error.getMessage());
            //error.printStackTrace();

        }
        finally {
            databaseDriver.close();
        }

    }

    @FXML
    public void exitdatabase(){
        if (databaseDriver != null){
            databaseDriver.close();
        }
    }
    public void handleReturnToLoginAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) messageLabel.getScene().getWindow();
            currentStage.close();
            exitdatabase();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeApplication() {
        exitdatabase();
        Platform.exit();

    }

}
