package ru.bvkuchin.networkchat.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AuthDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField SignInLoginField;

    @FXML
    private TextField SignInPasswordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpLoginField;

    @FXML
    private TextField signUpPassField;

    @FXML
    private TextField signUpReinsertPassField;

    @FXML
    void onSignInPressed(ActionEvent event) {

    }

    @FXML
    void onSignUpPressed(ActionEvent event) {

    }

    @FXML
    void initialize() {
    
    }

}
