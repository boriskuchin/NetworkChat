package ru.bvkuchin.networkchat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.bvkuchin.networkchat.components.CompletedTab;
import ru.bvkuchin.networkchat.components.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloController {

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy HH:mm:ss");

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private TextArea textEnterField;

    @FXML
    private Button sendButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private ListView<String> usersList;

    Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        sendMessage();
    }

    void sendMessage() {
        sendMessageToConversationList();
        sendMessageToServer();
        clearInputField();
    }

    private void sendMessageToServer() {
        connection.sendMessage(textEnterField.getText());
    }

    protected void sendMessageToConversationList() {
        CompletedTab tab = (CompletedTab) tabPane.getSelectionModel().getSelectedItem();
        if ((textEnterField.getText().length() != 0) && (tabPane.getSelectionModel().getSelectedItem() != null)) {
            ListView<String> conversationList = ((CompletedTab) tabPane.getSelectionModel().getSelectedItem()).getConvertsationHistory();
            conversationList.getItems().add(String.format("%s: %n%s", dateFormat.format(new Date()).toString(), textEnterField.getText()));
            conversationList.scrollTo(conversationList.getItems().size() - 1);
        }
    }

    @FXML
    void onClosePressed(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onUserListClicked(MouseEvent event) { System.out.println("onUserListClicked method"); }

    @FXML
    void initialize() {

        usersList.getItems().add("Жорик");
        usersList.getItems().add("Толик");
        usersList.getItems().add("Жмурик");
        usersList.getItems().add("Дурик");


        usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        String name = usersList.getSelectionModel().getSelectedItem();
                        for (Tab t : tabPane.getTabs()) {
                            if (t.getText().equals(name)) {
                                return;
                            }
                        }
                        tabPane.getTabs().add(new CompletedTab(name));
                    }
                }
            }
        });

    }

    public void getMessageFromInputStream() {
        Thread t = new Thread(() -> {


            while (true) {
                try {
                    String message = connection.readMessage();

                    final ListView<String> conversationList = ((CompletedTab) tabPane.getSelectionModel().getSelectedItem()).getConvertsationHistory();
                    Platform.runLater(() -> {

                        conversationList.getItems().add(message);
                        conversationList.scrollTo(conversationList.getItems().size());
                    });
                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        });
        t.setDaemon(true);
        t.start();


    }


    private void clearInputField() {
        textEnterField.setText("");
    }

}

