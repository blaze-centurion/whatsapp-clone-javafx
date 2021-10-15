package sample.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sample.controller.utils.EventHandler;
import sample.utils.Contact;
import sample.utils.Database;
import sample.utils.Utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientUiController {
    @FXML
    private VBox contactListBox, addContactListBox;
    @FXML
    private StackPane stackWindow;
    @FXML
    private Circle userProfileAvatar, profileSettingUserAvatar;
    @FXML
    private BorderPane chatBorderPane;
    @FXML
    private Pane addContactPane, profileSettingPane;
    @FXML
    private FontAwesomeIcon editableIcon;
    @FXML
    private TextField whatsappIdProfileLabel, searchChatInput, profileNameInput, contactInput;

    private Database database = new Database();
    private Utils utils = new Utils();
    private int userId;
    private String userName;
    private String userProfile;
    private String email;
    private String notFoundErrString = "No contact found. Make sure you are using right user id.";

    EventHandler refetchContactItem = new EventHandler() {
        @Override
        public void handle() {
            contactListBox.getChildren().clear();
            configureContactBox();
        }
    };

    @FXML
    public void openAddContact() {
        utils.slideAnimation(0, addContactPane);
    }

    @FXML
    void openProfileSetting() {
        utils.slideAnimation(0, profileSettingPane);
    }

    @FXML
    void backToContactForProfile() {
        utils.slideAnimation(-(profileSettingPane.getWidth()), profileSettingPane);
    }

    @FXML
    void backToContact() {
        utils.slideAnimation(-(addContactPane.getWidth()), addContactPane);
    }

    @FXML
    void toggleEditable() {
        boolean isEditable = profileNameInput.isEditable();
        if (isEditable) {
            String n = profileNameInput.getText();
            if (!n.equals(userName)) database.executeUpdateQuery(String.format("UPDATE users SET name = '%s' WHERE whatsappId = %d", n, userId));
            editableIcon.setGlyphName("PENCIL");
        } else editableIcon.setGlyphName("CHECK");
        profileNameInput.setEditable(!isEditable);
    }

    @FXML
    void searchChatItem() {
        ArrayList<Contact> contacts = getContactItemsFromName();
        contactListBox.getChildren().clear();
        for (Contact contact : contacts) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ContactItem.fxml"));
                AnchorPane item = loader.load();
                ContactItemController cic = loader.getController();
                cic.setRefetchContactItem(refetchContactItem);
                cic.setData(contact.getName(), contact.getProfile(), contact.getWhatsappId(), userId);
                cic.setChatBorderPane(chatBorderPane);
                cic.setStackWindow(stackWindow);
                contactListBox.getChildren().add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void searchContact() {
        String s = contactInput.getText();
        if (!utils.isNumberOnly(s) || s.length()!=9) {
            Label label = new Label(notFoundErrString);
            utils.showNotFoundErr(addContactListBox, label);
            return;
        }
        populateAddContactBox();
    }

    public void setUserInfo(String userName, String email, int userId, String userProfile) {
        this.userName = userName;
        this.email = email;
        this.userId = userId;
        this.userProfile = userProfile;
        utils.setProfile(this.userProfile, userProfileAvatar);
        configureContactBox();
        configureProfilePage();
    }

    private void configureProfilePage() {
        Image img = new Image(userProfile);
        profileSettingUserAvatar.setFill(new ImagePattern(img));
        whatsappIdProfileLabel.setText(String.valueOf(userId));
        profileNameInput.setText(userName);
    }

    private void configureContactBox() {
        ArrayList<Contact> contacts = getContactItems();
        for (Contact contact : contacts) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ContactItem.fxml"));
                AnchorPane item = loader.load();
                ContactItemController cic = loader.getController();
                cic.setRefetchContactItem(refetchContactItem);
                cic.setData(contact.getName(), contact.getProfile(), contact.getWhatsappId(), userId);
                cic.setChatBorderPane(chatBorderPane);
                cic.setStackWindow(stackWindow);
                contactListBox.getChildren().add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void populateAddContactBox() {
        ArrayList<Contact> contacts = getAddContactItem();
        if (contacts.size()==0) {
            utils.showNotFoundErr(addContactListBox, new Label(notFoundErrString));
            return;
        }
        addContactListBox.getChildren().clear();
        for (Contact contact : contacts) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AddContactItem.fxml"));
                AnchorPane item = loader.load();
                AddContactItemController addContactItemController = loader.getController();
                addContactItemController.setUserDetails(contact.getName(), contact.getProfile(), contact.getWhatsappId(), userId, refetchContactItem);
                addContactListBox.getChildren().add(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Contact> getContactItems() {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            ResultSet rs = database.executeQuery("SELECT * FROM contacts JOIN users ON users.whatsappId = contacts.contactId WHERE contacts.userId = " + userId);
            while (rs.next()) {
                String profile = rs.getString("userProfile");
                Contact contact = new Contact(rs.getInt("whatsappId"), rs.getString("name"), profile);
                contacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contacts;
    }

    private ArrayList<Contact> getContactItemsFromName() {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            ResultSet rs = database.executeQuery("SELECT * FROM contacts JOIN users ON users.whatsappId = contacts.contactId WHERE contacts.userId = " + userId + " AND users.name LIKE '%"+searchChatInput.getText()+"%'");
            while (rs.next()) {
                String profile = rs.getString("userProfile");
                Contact contact = new Contact(rs.getInt("whatsappId"), rs.getString("name"), profile);
                contacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contacts;
    }

    private ArrayList<Contact> getAddContactItem() {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            ResultSet rs = database.executeQuery(String.format("SELECT * FROM users WHERE whatsappId = %d AND whatsappId != %d", Integer.parseInt(contactInput.getText()), userId));
            while (rs.next()) {
                Contact contact = new Contact(rs.getInt("whatsappId"), rs.getString("name"), rs.getString("userProfile"));
                contacts.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contacts;
    }
}
