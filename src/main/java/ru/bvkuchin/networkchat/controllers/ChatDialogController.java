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
import org.apache.commons.io.input.ReversedLinesFileReader;
import ru.bvkuchin.networkchat.ChatApplication;
import ru.bvkuchin.networkchat.components.Connection;
import ru.bvkuchin.networkchat.components.Prefix;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private String login;


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
        String msg;
        String name;

        if (!isMessagePrivate) {
            msg = String.format("%s: %nЯ: %s", dateFormat.format(new Date()), textEnterField.getText());
        } else {
            name = usersList.getSelectionModel().getSelectedItem();
            msg = String.format("%s: %nЯ [PM] %s: %s", dateFormat.format(new Date()), name, textEnterField.getText());
        }

        convHistoryList.getItems().add(msg);
        saveHistory(msg);
        convHistoryList.scrollTo(convHistoryList.getItems().size() - 1);

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
                    System.out.println(message);

                    switch (Prefix.getPrefixFromText( message.split("\\s+", 2)[0])) {
                        case LIST_CLIENTS_CMD_PREFIX:
                            Platform.runLater(() -> {
                                usersList.getItems().clear();
                                usersList.getItems().addAll(message.split("\\s+", 2)[1].trim().split("\\s+"));
                            });
                            break;
                        default:
                            saveHistory(message);
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

    public void setLogin(String login) {
        this.login = login;
    }

    public void loadHistolyFromLog(String login) {

        ArrayList<String> convHistory = new ArrayList<>(100);
        File chatHistoryPath = new File("src/main/resources/history");
        File chatHistoryFile = new File(chatHistoryPath, login + ".log");

        if (Files.exists(chatHistoryFile.toPath())) {

            int n_lines = 100;
            int counter = 0;
            String line;
            try (ReversedLinesFileReader reversedLinesFileReader = new ReversedLinesFileReader(chatHistoryFile)) {
                while (((line = reversedLinesFileReader.readLine()) != null) && counter < n_lines) {
                    convHistory.add(line);
                    counter++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Collections.reverse(convHistory);


        StringBuilder sb = new StringBuilder();
        for (String s : convHistory) {
            sb.append(s + System.lineSeparator());
        }

        convHistoryList.getItems().add(sb.toString());
        }
    }

    private void saveHistory(String message) {

        File chatHistoryPath = new File("src/main/resources/history");
        File chatHistoryFile = new File(chatHistoryPath, login + ".log");

        String msg_to_save = message + System.lineSeparator();
        try (FileOutputStream out = new FileOutputStream(chatHistoryFile, true)) {
            out.write(msg_to_save.getBytes());
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

