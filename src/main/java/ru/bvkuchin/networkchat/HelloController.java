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

    }

    @FXML
    void onSendButtonClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert menuItemClose != null : "fx:id=\"menuItemClose\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert menuItemAbout != null : "fx:id=\"menuItemAbout\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert conversationTextField != null : "fx:id=\"conversationTextField\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert textEnterField != null : "fx:id=\"textEnterField\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'hello-view.fxml'.";

    }
}
