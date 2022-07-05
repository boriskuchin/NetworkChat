package ru.bvkuchin.networkchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Stage;
import ru.bvkuchin.networkchat.components.Connection;

import java.io.IOException;

public class HelloApplication extends Application {

    Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("networkChat-view.fxml"));
        Parent rootPane = root.load();
        Scene scene = new Scene(rootPane);
        stage.setTitle("Network chat!");
        stage.setScene(scene);

//        вытаскиваем экземпляр контроллера из лоудера
        HelloController controller = root.getController();
        connection = new Connection();
        controller.setConnection(connection);
        connection.connect();

//        Добавеление слушателя комбинаций на сцену
        KeyCombination C_Enter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (C_Enter.match(evt)) {
                controller.sendMessage();

            }
        });

        controller.getMessageFromInputStream();



        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}