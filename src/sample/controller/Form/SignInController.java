package sample.controller.Form;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.utils.BCrypt;
import sample.utils.Database;
import sample.utils.Utils;
import sample.controller.ClientUiController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignInController implements Initializable{
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label errorLabel;

    private void showNewStage(Stage oldStage, String path, String userName, String email, int userId, String userProfile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            ClientUiController uiController = loader.getController();
            uiController.setUserInfo(userName, email, userId, userProfile);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            oldStage.close();
            stage.show();
            stage.setOnCloseRequest(windowEvent -> {
                Platform.exit();
                System.exit(-1);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signIn(ActionEvent event) {
        Utils utils = new Utils();
        if (email.getText().length() != 0 && password.getText().length() != 0 ) {
            Database db = new Database();
            try {
                Statement st = db.getConnection().createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM users WHERE email = '"+email.getText()+"'");
                if (rs.next()) {
                    if (BCrypt.checkpw(password.getText(), rs.getString("password"))) {
                        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        showNewStage(oldStage, "../../fxml/ClientUi.fxml", rs.getString("name"), rs.getString("email"), rs.getInt("whatsappId"), rs.getString("userProfile"));
                    } else {
                        utils.showError(errorLabel, "Invalid credentials");
                    }
                } else {
                    utils.showError(errorLabel, "Invalid credentials");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            utils.showError(errorLabel, "Please fill all the fields");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email.setText("roshan@mail.com");
        password.setText("roshan");
    }
}
