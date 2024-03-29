package ru.bvkuchin.networkchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.bvkuchin.networkchat.components.Connection;
import ru.bvkuchin.networkchat.controllers.AuthDialogController;
import ru.bvkuchin.networkchat.controllers.ChangeNameController;
import ru.bvkuchin.networkchat.controllers.ChatDialogController;

import java.io.IOException;

public class ChatApplication extends Application {

    private Connection connection;
    private Stage chatStage;
    private Stage authStage;
    private AuthDialogController authDialogController;
    private ChatDialogController chatDialogController;
    private Stage changeNameStage;
    private ChangeNameController changeNameDialogController;


    @Override
    public void start(Stage stage) throws IOException {
        chatStage = stage;
        connection = new Connection();
        connection.connect();

        openAuthDialog();
        createChatDialog();
        createChangeNameDialog();

        //        Добавеление слушателя комбинаций на сцену
        KeyCombination C_Enter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (C_Enter.match(evt)) {
                chatDialogController.sendMessage();
            }
        });
    }

    private void createChatDialog() throws IOException {
        FXMLLoader chatLoader = new FXMLLoader(ChatApplication.class.getResource("networkChat-view.fxml"));
        Scene scene = new Scene(chatLoader.load());

        chatStage.setScene(scene);
        chatStage.setAlwaysOnTop(true);

        chatDialogController = chatLoader.getController();
        chatDialogController.setConnection(connection);
        chatDialogController.setApplichtion(this);

    }

    private void openAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader(ChatApplication.class.getResource("authChat-view.fxml"));
        authStage = new Stage();

        Scene scene = new Scene(authLoader.load());

        authStage.setScene(scene);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(chatStage);
        authStage.setTitle("Authentication!");
        authStage.setAlwaysOnTop(true);

        authStage.show();

        authDialogController = authLoader.getController();
        authDialogController.setApplication(this);
        authDialogController.setConnection(connection);

    }

    private void createChangeNameDialog() throws IOException {
        FXMLLoader changeNameLoader = new FXMLLoader(ChatApplication.class.getResource("changeName-view.fxml"));
        changeNameStage = new Stage();

        Scene scene = new Scene(changeNameLoader.load());

        changeNameStage.setScene(scene);
        changeNameStage.initModality(Modality.WINDOW_MODAL);
        changeNameStage.initOwner(chatStage);
        changeNameStage.setTitle("Change Name!");
        changeNameStage.setAlwaysOnTop(true);


        changeNameDialogController = changeNameLoader.getController();
        changeNameDialogController.setApplication(this);
        changeNameDialogController.setConnection(connection);

    }


    public void openChatDialog(String name, String login) {
        authStage.close();
        setChatTitle(name);
        chatStage.show();
        chatDialogController.loadHistolyFromLog(login);
        chatDialogController.getMessageFromInputStream();
        chatDialogController.setLogin(login);
    }

    public void showErrorAlert(String title, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setX(10);
        alert.setY(10);
        alert.setHeaderText(errorMessage);
        alert.show();
    }

    public void showInfoAlert(String title, String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setX(10);
        alert.setY(10);
        alert.setTitle(title);
        alert.setHeaderText(infoMessage);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openChangeNameDialog() throws IOException {
        changeNameStage.show();
    }

    public void closeChangeNameDialog() {
        changeNameStage.close();
    }

    public void setChatTitle(String login) {
        chatStage.setTitle("Network chat! Login: " + login);

    }
}