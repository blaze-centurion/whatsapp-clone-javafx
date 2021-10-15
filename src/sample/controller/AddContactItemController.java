package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import sample.controller.utils.EventHandler;
import sample.utils.Database;

public class AddContactItemController {

    @FXML
    private Circle profileAvatar;

    @FXML
    private Text userNameLabel;

    private String userName;
    private int contactId;
    private int userId;
    private String userProfile;
    private EventHandler refetchContacts;

    public void setUserDetails(String userName, String userProfile, int contactId, int userId, EventHandler refetchContacts) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.contactId = contactId;
        this.refetchContacts = refetchContacts;
        userNameLabel.setText(userName);
        Image img = new Image(userProfile);
        profileAvatar.setFill(new ImagePattern(img));
    }

    @FXML
    void addContact() {
        (new Database()).executeUpdateQuery(String.format("INSERT INTO contacts (userId, contactId) VALUES (%d, %d), (%d, %d)", userId, contactId, contactId, userId));
        refetchContacts.handle();
    }

}
