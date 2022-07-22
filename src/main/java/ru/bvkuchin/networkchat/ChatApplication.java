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
import ru.bvkuchin.networkchat.controllers.ChatDialogController;

import java.io.IOException;

public class ChatApplication extends Application {

    private Connection connection;
    private Stage chatStage;
    private Stage authStage;
    private AuthDialogController authDialogController;
    private ChatDialogController chatDialogController;



    @Override
    public void start(Stage stage) throws IOException {
        chatStage = stage;
        connection = new Connection();
        connection.connect();

        openAuthDialog();
        createChatDialog();

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


    public void openChatDialog(String login) {
        authStage.close();
        chatStage.setTitle("Network chat! Login: " + login);
        chatStage.show();
        chatDialogController.getMessageFromInputStream();
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
}