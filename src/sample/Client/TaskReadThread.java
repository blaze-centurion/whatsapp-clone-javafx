package sample.Client;

/*
 * It is use to get input from server simultaneously
 */

import javafx.application.Platform;
import sample.controller.ChatPageController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TaskReadThread implements Runnable {
    Socket socket;
    ChatPageController client;
    DataInputStream inputStream;

    public TaskReadThread(Socket socket, ChatPageController client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                String msg = inputStream.readUTF();
                Platform.runLater(() -> client.displayMessage(msg));
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
