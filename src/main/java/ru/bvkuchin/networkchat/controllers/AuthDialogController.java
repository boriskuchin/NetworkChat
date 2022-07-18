package ru.bvkuchin.networkchat.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import ru.bvkuchin.networkchat.ChatApplication;
import ru.bvkuchin.networkchat.components.Connection;
import ru.bvkuchin.networkchat.components.Prefixes;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthDialogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signInLoginField;

    @FXML
    private TextField signInPasswordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpLoginField;

    @FXML
    private TextField signUpNameField;

    @FXML
    private TextField signUpPassField;
    private Connection connection;

    @Setter
    private ChatApplication application;

    @FXML
    void onSignInPressed(ActionEvent event) throws Exception {
        String login = signInLoginField.getText();
        String password = signInPasswordField.getText();
        connection.sendMessage(String.format("%s %s %s", Prefixes.AUTH_CMD_PREFIX.getPrefix(), login, password));
        String serverResponse = connection.readMessage();
        System.out.println(serverResponse);

        if (serverResponse.startsWith(Prefixes.AUTHOK_CMD_PREFIX.getPrefix())) {
            application.openChatDialog();
        } else if (serverResponse.startsWith(Prefixes.AUTHERR_CMD_PREFIX.getPrefix())) {

            application.showErrorAlert("Ошибка авторизации", serverResponse.trim().split("\\s+", 2)[1]);
        } else {
            application.showErrorAlert("Ошибка авторизации", serverResponse);
        }
    }

    @FXML
    void onSignUpPressed(ActionEvent event) throws Exception {
        String signUpLogin = signUpLoginField.getText();
        String signUpPass = signUpPassField.getText();
        String signUpName = signUpNameField.getText();

        connection.sendMessage(String.format("%s %s %s %s", Prefixes.NEW_USR_CMD_PREFIX.getPrefix(), signUpLogin, signUpName, signUpPass));
        String serverResponse = connection.readMessage();
        System.out.println(serverResponse);

        if (serverResponse.startsWith(Prefixes.NEW_USR_OK_CMD_PREFIX.getPrefix())) {
            application.showInfoAlert("Регистрация прошла успешно", "Залогиньтесь с вашим логином и паролем");
            signUpLoginField.setText("");
            signUpPassField.setText("");
            signUpNameField.setText("");

        } else if (serverResponse.startsWith(Prefixes.NEW_USR_ERR_CMD_PREFIX.getPrefix())) {

            application.showErrorAlert("Ошибка регистрации", serverResponse.trim().split("\\s+", 2)[1]);
        } else {
            application.showErrorAlert("Ошибка регистрации", serverResponse);
        }

    }

    @FXML
    void initialize() {

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
