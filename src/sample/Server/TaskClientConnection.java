/*
* Handle the peer conn between clients and broadcast the messages
*/

package sample.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class TaskClientConnection implements Runnable{
    private Server server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    List<TaskClientConnection> connectionList;
    public int receiverId;
    public int userId;

    public TaskClientConnection(Server server, Socket socket, List<TaskClientConnection> connectionList) {
        this.server = server;
        this.socket = socket;
        this.connectionList = connectionList;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            String input = inputStream.readUTF();
            receiverId = Integer.parseInt(input.substring(input.length()-9));
            userId = Integer.parseInt(input.substring(0, input.length()-9));
            server.addNewUserId(receiverId);

            while (true) {
                String msg = inputStream.readUTF();
                server.broadcast(msg, this);
            }
        } catch (IOException e) {
            server.removeUser(this);
            System.out.println("Error from catch clause in TaskClientConnection.java: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error from final clause in TaskClientConnection.java: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            outputStream.writeUTF(msg);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error from catch clause of send msg in TaskClientConnection.java: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
