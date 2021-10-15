package sample.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import sample.controller.utils.DropDown;
import sample.controller.utils.DropDownItem;
import sample.controller.utils.EventHandler;
import sample.utils.Database;
import sample.utils.Utils;

public class ChatItemController {
    @FXML
    private HBox chatItem;
    @FXML
    private AnchorPane chatItemContentBox;
    @FXML
    private Label msgLabel;
    @FXML
    private Label timeLabel;

    private Utils utils = new Utils();
    private StackPane stackWindow;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    void openDropDown(MouseEvent e) {
        DropDown dropDown = new DropDown(null, stackWindow);
        DropDownItem dropDownItem = new DropDownItem("Delete Message", new EventHandler() {
            @Override
            public void handle() {
                Database db = new Database();
                db.executeUpdateQuery("DELETE FROM chats WHERE id = " + id);
            }
        });
        dropDown.add(dropDownItem);
        dropDown.show(e.getSceneX(), e.getSceneY());
    }

    public void setStackWindow(StackPane stackWindow) {
        this.stackWindow = stackWindow;
    }

    public void sendMsg(String msg) {
        msgLabel.setText(msg);
        timeLabel.setText(utils.getCurrTime());
    }

    public void showMsg(String msg, String time) {
        msgLabel.setText(msg);
        timeLabel.setText(time);
    }

    public void setChatItemAlignment(Pos pos) {
        chatItem.setAlignment(pos);
    }

    public void setChatItemSenderClass() {
        chatItemContentBox.getStyleClass().add("me");
    }

}
