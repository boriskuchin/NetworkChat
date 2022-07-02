package ru.bvkuchin.networkchat;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

public class HelloController {

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy HH:mm:ss");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private ListView<String> conversationList;

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
        sendMessage();
    }

    protected void sendMessage() {
        if (textEnterField.getText().length() != 0) {
            conversationList.getItems().add( dateFormat.format(new Date()).toString() + ": " + textEnterField.getText());
            textEnterField.setText("");
        }
    }


    @FXML
    void onClosePressed(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void initialize() {

    }
}
