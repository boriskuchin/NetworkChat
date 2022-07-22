package ru.bvkuchin.networkchat.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import ru.bvkuchin.networkchat.ChatApplication;
import ru.bvkuchin.networkchat.components.Connection;
import ru.bvkuchin.networkchat.components.Prefix;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatDialogController {


    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy HH:mm:ss", Locale.ROOT);


    @FXML
    private MenuItem menuItemClose;

    @FXML
    private TextArea textEnterField;

    @FXML
    private Button sendButton;

    @FXML
    private ListView<String> convHistoryList;

    @FXML
    private ListView<String> usersList;

    @FXML
    private MenuItem menuItemServerStop;

    @FXML
    private MenuItem menuItemChangeName;




    Connection connection;
    boolean isMessagePrivate = false;
    private ChatApplication chatApplication;


    @FXML
    void OnServerStopButton(ActionEvent event) {
        connection.sendMessage(Prefix.STOP_SERVER_CMD_PREFIX.getPrefix());
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        sendMessage();
    }

   public void sendMessage() {
        sendMessageToConversationList();
        sendMessageToServer();
        clearInputField();
    }

    protected void sendMessageToServer() {
        if (!isMessagePrivate) {

            connection.sendMessage(String.format("%s %s", Prefix.CLIENT_MSG_CMD_PREFIX.getPrefix(), textEnterField.getText()));
        } else {
            String name = usersList.getSelectionModel().getSelectedItem();
            connection.sendMessage(String.format("%s %s %s", Prefix.PRIVATE_MSG_CMD_PREFIX.getPrefix(),name, textEnterField.getText()));
        }
    }

    protected void sendMessageToConversationList() {
        if (!isMessagePrivate) {
            convHistoryList.getItems().add(String.format("%s: %nЯ: %s", dateFormat.format(new Date()).toString(), textEnterField.getText()));
            convHistoryList.scrollTo(convHistoryList.getItems().size() - 1);
        } else {
            String name = usersList.getSelectionModel().getSelectedItem();
            convHistoryList.getItems().add(String.format("%s: %nЯ [PM] %s: %s", dateFormat.format(new Date()).toString(), name, textEnterField.getText()));
            convHistoryList.scrollTo(convHistoryList.getItems().size() - 1);
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

       usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){

                        if (!isMessagePrivate) {
                            isMessagePrivate = !isMessagePrivate;
                            sendButton.setTextFill(Color.RED);
                            sendButton.setText(String.format("PM %nSend"));
                        } else {
                            isMessagePrivate = !isMessagePrivate;
                            sendButton.setTextFill(Color.rgb(18,162,181));
                            sendButton.setText(String.format("Send"));
                        }

                    }
                }
            }
        });

    }

    public void getMessageFromInputStream() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String message = connection.readMessage();

                    switch (Prefix.getPrefixFromText( message.split("\\s+", 2)[0])) {
                        case LIST_CLIENTS_CMD_PREFIX:
                            Platform.runLater(() -> {
                                usersList.getItems().clear();
                                usersList.getItems().addAll(message.split("\\s+", 2)[1].trim().split("\\s+"));
                            });
                            break;
                        case CLIENT_MSG_CMD_PREFIX:
                            Platform.runLater(() -> {
                                convHistoryList.getItems().add(message.split("\\s+", 2)[1]);
                                convHistoryList.scrollTo(convHistoryList.getItems().size());
                            });
                            break;
                        case SERVER_MSG_CMD_PREFIX:
                            Platform.runLater(() -> {
                                convHistoryList.getItems().add("ВНИМАНИЕ! "+ message.split("\\s+", 2)[1].toUpperCase());
                                convHistoryList.scrollTo(convHistoryList.getItems().size());
                            });
                            break;
                        case PRIVATE_MSG_CMD_PREFIX:
                            Platform.runLater(() -> {
                                convHistoryList.getItems().add("[PM] "+ message.split("\\s+", 2)[1]);
                                convHistoryList.scrollTo(convHistoryList.getItems().size());
                            });
                            break;
                        default:
                            Platform.runLater(() -> {
                                convHistoryList.getItems().add(message);
                                convHistoryList.scrollTo(convHistoryList.getItems().size());
                            });
                    }
                }
            } catch (Exception e) {
                System.out.println("Соединение разорвано сервером. Перезапустите сервер и подключитесь снова");
                e.printStackTrace();
            }
        });

        t.start();
    }

    private void clearInputField() {
        textEnterField.setText("");
    }

    @FXML
    void onChangeNamePressed(ActionEvent event) throws IOException {
        chatApplication.openChangeNameDialog();
    }

    public void setApplichtion(ChatApplication chatApplication) {
        this.chatApplication = chatApplication;
    }
}

