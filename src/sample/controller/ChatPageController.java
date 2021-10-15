package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sample.Client.TaskReadThread;
import sample.controller.utils.DropDown;
import sample.controller.utils.DropDownItem;
import sample.controller.utils.EventHandler;
import sample.utils.ConnectionUtil;
import sample.utils.Database;
import sample.utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatPageController {
    @FXML
    private Circle profileAvatar;
    @FXML
    private Text name;
    @FXML
    private ScrollPane chatScrollpane;
    @FXML
    private TextField chatInput;
    @FXML
    private VBox chatBox;

    private StackPane stackWindow;
    private String contactName;
    private int contactId;
    private String contactProfile;
    private DataOutputStream outputStream;
    private int userId;
    private boolean scrollToBottom = false;
    private Database db = new Database();
    private Utils utils = new Utils();
    EventHandler refetchContactItem;
    private BorderPane chatBorderPane;

    @FXML
    void openDropDown(MouseEvent event) {
        DropDown dropDown = new DropDown(null, this.stackWindow);
        dropDown.add(new DropDownItem("Delete Message", new EventHandler() {
            @Override
            public void handle() {
                db.executeUpdateQuery(String.format("DELETE FROM chats WHERE senderId IN (%d, %d) AND receiverId IN (%d, %d)", userId, contactId, contactId, userId));
            }
        }));
        dropDown.add(new DropDownItem("Delete Contact", new EventHandler() {
            @Override
            public void handle() {
                db.executeUpdateQuery(String.format("DELETE FROM contacts WHERE userId = %d AND contactId = %d", userId, contactId));
                chatBorderPane.setCenter(null);
                refetchContactItem.handle();
            }
        }));
        dropDown.show(event.getSceneX()-240, event.getSceneY()+20);
    }

    private void loadMsg() {
        try {
            String q = String.format("SELECT * FROM chats WHERE senderId IN (%d, %d) AND receiverId IN (%d, %d)", userId, contactId, contactId, userId);
            ResultSet rs = db.executeQuery(q);
            while (rs.next()) {
                displayMessage(rs.getString("msg"), rs.getInt("senderId"), rs.getString("time"), rs.getInt("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createConn() {
        try {
            Socket socket = new Socket(ConnectionUtil.host, ConnectionUtil.port);
            System.out.println("Connected");
            outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println(userId);
            outputStream.writeUTF(String.valueOf(userId) + contactId);
            outputStream.flush();
            TaskReadThread task = new TaskReadThread(socket, this);
            (new Thread(task)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContactDetails(String contactName, String contactProfile, int contactId, int userId, StackPane stackWindow, EventHandler refetchContactItem, BorderPane chatBorderPane) {
        this.contactName = contactName;
        this.contactProfile = contactProfile;
        this.contactId = contactId;
        this.userId = userId;
        this.stackWindow = stackWindow;
        this.refetchContactItem = refetchContactItem;
        this.chatBorderPane = chatBorderPane;
        this.name.setText(contactName);
        createConn();
        (new Utils()).setProfile(contactProfile, profileAvatar);
        loadMsg();
    }

    public void displayMessage(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ChatItem.fxml"));
            HBox hBox = loader.load();
            ChatItemController chatItemController = loader.getController();
            chatItemController.sendMsg(msg);
            chatItemController.setStackWindow(stackWindow);
            chatAlignmentAndSaveMsg(chatItemController);
            chatBox.getChildren().add(hBox);
            scrollToBottom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void displayMessage(String msg, Pos pos) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ChatItem.fxml"));
            HBox hBox = loader.load();
            ChatItemController chatItemController = loader.getController();
            chatItemController.sendMsg(msg);
            chatItemController.setStackWindow(stackWindow);
            chatAlignment(pos, chatItemController);
            chatBox.getChildren().add(hBox);
            scrollToBottom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayMessage(String msg, int senderId, String time, int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ChatItem.fxml"));
            HBox hBox = loader.load();
            ChatItemController chatItemController = loader.getController();
            chatItemController.showMsg(msg, time);
            chatItemController.setId(id);
            chatItemController.setStackWindow(stackWindow);
            if (senderId == userId) {
                chatItemController.setChatItemAlignment(Pos.CENTER_RIGHT);
                chatItemController.setChatItemSenderClass();
            }
            else chatItemController.setChatItemAlignment(Pos.CENTER_LEFT);
            chatBox.getChildren().add(hBox);
            scrollToBottom();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMessage(String msg) {
        db.executeUpdateQuery(String.format("INSERT INTO `chats` (senderId, receiverId, msg, date, time) VALUES (%d, %d, '%s', '%s', '%s')", userId, contactId, msg, utils.getCurrDate(), utils.getCurrTime()));
    }

    private void scrollToBottom() {
        scrollToBottom = true;
        chatScrollpane.vvalueProperty().addListener((observableValue, number, t1) -> {
            if (scrollToBottom) {
                chatScrollpane.setVvalue(chatScrollpane.getVmax());
                scrollToBottom = false;
            }
        });
    }

    private void chatAlignmentAndSaveMsg(ChatItemController chatItemController) {
        chatItemController.setChatItemAlignment(Pos.CENTER_LEFT);
    }

    private void chatAlignment(Pos pos, ChatItemController chatItemController) {
        chatItemController.setChatItemAlignment(pos);
        chatItemController.setChatItemSenderClass();
    }

    @FXML
    void sendChat() {
        try {
            String msg = chatInput.getText().trim();
            if (msg.length()==0) return;
            saveMessage(msg);
            displayMessage(msg, Pos.CENTER_RIGHT);
            outputStream.writeUTF(msg);
            outputStream.flush();
            chatInput.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
