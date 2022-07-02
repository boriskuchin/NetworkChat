package ru.bvkuchin.networkchat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(HelloApplication.class.getResource("networkChat-view.fxml"));
        Parent rootPane = root.load();
        Scene scene = new Scene(rootPane);
        stage.setTitle("Network chat!");
        stage.setScene(scene);

//        вытаскиваем экземпляр контроллера из лоудера
        HelloController controller = root.getController();

//        Добавеление слушателя комбинаций на сцену
        KeyCombination C_Enter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, evt -> {
            if (C_Enter.match(evt)) {
                controller.sendMessage();
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}