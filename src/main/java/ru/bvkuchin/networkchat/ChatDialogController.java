package ru.bvkuchin.networkchat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import ru.bvkuchin.networkchat.components.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatDialogController {

    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX = "/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";
    private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg";
    private static final String SERVER_MSG_CMD_PREFIX = "/sMsg";
    private static final String PRIVATE_MSG_CMD_PREFIX = "/pm";
    private static final String STOP_SERVER_CMD_PREFIX = "/stop";
    private static final String END_CLIENT_CMD_PREFIX = "/end";
    private static final String LIST_CLIENTS_CMD_PREFIX = "/usrs";


    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy HH:mm:ss");

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

    Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        sendMessage();
    }

    void sendMessage() {
//        sendMessageToConversationList();
        sendMessageToServer();
        clearInputField();
    }

    private void sendMessageToServer() {
        connection.sendMessage(textEnterField.getText());
    }

    protected void sendMessageToConversationList() {
            convHistoryList.getItems().add(String.format("%s: %n%s", dateFormat.format(new Date()).toString(), textEnterField.getText()));
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

//        usersList.getItems().add("Жорик");
//        usersList.getItems().add("Толик");
//        usersList.getItems().add("Жмурик");
//        usersList.getItems().add("Дурик");


       /* usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        });*/

    }

    public void getMessageFromInputStream() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String message = connection.readMessage();

                    switch (message.split("\\s+", 2)[0]) {
                        case LIST_CLIENTS_CMD_PREFIX:
                            Platform.runLater(() -> {
                                usersList.getItems().clear();
                                usersList.getItems().addAll(message.split("\\s+", 2)[1].trim().split("\\s+"));
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

}

