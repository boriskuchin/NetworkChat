package ru.bvkuchin.networkchat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private ListView<?> conversationTextField;

    @FXML
    private TextArea textEnterField;

    @FXML
    private Button sendButton;

    @FXML
    void onEnterPress(KeyEvent event) {
        System.out.println("Enter" +
                " pressed");
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        System.out.println("Send pressed");

    }


    @FXML
    void onClosePressed(ActionEvent event) {
        System.exit(0);
    }



    @FXML
    void initialize() {

    }
}
