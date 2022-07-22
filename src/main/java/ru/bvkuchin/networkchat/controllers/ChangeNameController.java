package ru.bvkuchin.networkchat.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.bvkuchin.networkchat.ChatApplication;
import ru.bvkuchin.networkchat.components.Connection;
import ru.bvkuchin.networkchat.components.Prefix;

public class ChangeNameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button changeNameButton;

    @FXML
    private TextField newNameField;
    private Connection connection;
    private ChatApplication application;

    @FXML
    void onChangeNameButtonPressed(ActionEvent event) {
        if (newNameField.getText().length() > 0) {
            connection.sendMessage(String.format("%s %s", Prefix.CNG_NAME_CMD_PREFIX.getPrefix(), newNameField.getText()));
            application.setChatTitle(newNameField.getText());
            newNameField.setText("");
            application.closeChangeNameDialog();
        } else {
            application.showErrorAlert("Ошибка изменения имени", "Имя не может быть пустое");
        }
    }

    @FXML
    void initialize() {

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setApplication(ChatApplication chatApplication) {
        this.application = chatApplication;
    }
}
