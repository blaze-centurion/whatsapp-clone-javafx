package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sample.controller.utils.DropDown;
import sample.controller.utils.DropDownItem;
import sample.controller.utils.EventHandler;
import sample.utils.Database;
import sample.utils.Utils;

import java.io.IOException;

public class ContactItemController {
    @FXML
    private ImageView dropdownBtn;
    @FXML
    private Circle profileAvatar;
    @FXML
    private Text userName;

    private StackPane stackWindow;
    private BorderPane chatBorderPane;
    private int recieverId; // Id of reciever
    private String name;
    private String profile;
    private int senderId; // Id of sender
    Utils utils = new Utils();
    Database db = new Database();
    EventHandler refetchContactItem;

    public void setData(String name, String profile, int recieverId, int senderId) {
        this.recieverId = recieverId;
        this.userName.setText(name);
        this.profile = profile;
        this.name = name;
        this.senderId = senderId;
        utils.setProfile(profile, profileAvatar);
    }

    public void setRefetchContactItem(EventHandler refetchContactItem) {
        this.refetchContactItem = refetchContactItem;
    }

    public void setStackWindow(StackPane stackWindow) {
        this.stackWindow = stackWindow;
    }

    public void setChatBorderPane(BorderPane chatBorderPane) {
        this.chatBorderPane = chatBorderPane;
    }

    @FXML
    void openDropdown(MouseEvent event) {
        DropDown dropDown = new DropDown(null, this.stackWindow);
        dropDown.add(new DropDownItem("Delete Message", new EventHandler() {
            @Override
            public void handle() {
                db.executeUpdateQuery(String.format("DELETE FROM chats WHERE senderId IN (%d, %d) AND receiverId IN (%d, %d)", senderId, recieverId, recieverId, senderId));
            }
        }));
        dropDown.add(new DropDownItem("Delete Contact", new EventHandler() {
            @Override
            public void handle() {
                db.executeUpdateQuery(String.format("DELETE FROM contacts WHERE userId = %d AND contactId = %d", senderId, recieverId));
                refetchContactItem.handle();
            }
        }));
        dropDown.show(event.getSceneX(), event.getSceneY());
    }

    @FXML
    void openContact(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        if ((x < 315 || x > 335) && (y < 47 || y > 67) ) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ChatPage.fxml"));
            try {
                BorderPane borderPane = loader.load();
                ChatPageController chatPageController = loader.getController();
                chatPageController.setContactDetails(name, profile, recieverId, senderId, stackWindow, refetchContactItem, chatBorderPane);
                chatBorderPane.setCenter(borderPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}