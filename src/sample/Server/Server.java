package sample.Server;

import sample.utils.ConnectionUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    List<TaskClientConnection> users = new ArrayList<>();
    List<Integer> userIds = new ArrayList<>();

    public static void main(String[] args) {
        (new Server()).start();
    }

    public void start() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(ConnectionUtil.port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    TaskClientConnection conn = new TaskClientConnection(this, socket, users);
                    users.add(conn);
                    Thread thread = new Thread(conn);
                    thread.start();
                }
            } catch (IOException e) {
                System.out.println("Error from catch clause in start method in Server.java: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
        System.out.println("Server started");
    }

    public void broadcast(String msg, TaskClientConnection excludeUser) {
        for (TaskClientConnection clientConnection : users) {
            if ((clientConnection != excludeUser) && (clientConnection.userId == excludeUser.receiverId && clientConnection.receiverId == excludeUser.userId)) {
                clientConnection.sendMsg(msg);
            }
        }
    }

    public void addNewUserId(int id) {
        userIds.add(id);
    }

    public void removeUser(TaskClientConnection user) {
        users.remove(user);
    }
}
