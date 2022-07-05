package ru.bvkuchin.networkchat.components;

import java.io.*;
import java.net.Socket;

public class Connection {

    private static final int PORT = 8186;
    private static final String SERVER_HOST = "localhost";
    Socket socket;

        DataInputStream inputStream;
    DataOutputStream outputStream;

    public Connection() {

    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOST, PORT);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream( socket.getOutputStream());


            System.out.println("Соединение установлено");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() {
        String message = null;

        try {
            message =  inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return message;
    }

    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
