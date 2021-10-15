package sample.controller.Form;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.utils.Database;
import sample.utils.Utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextField fullName;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private Label errorLabel;

    private boolean isEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        // Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        // Create instance of matcher
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    void signUp() {
        Utils utils = new Utils();
        if (email.getText().length() != 0 && password.getText().length() != 0 && fullName.getText().length() !=0) {
            if (!isEmail(email.getText())) {
                utils.showError(errorLabel, "Invalid email");
                return;
            };
            Database db = new Database();
            String pwd = utils.hashPassword(password.getText());
            int whatsappId = (new Random()).nextInt(900000000) + 100000000;
            db.executeUpdateQuery(String.format("INSERT INTO `users` (name, whatsappId, email, password) VALUES  ('%s', %d, '%s', '%s')", fullName.getText(), whatsappId, email.getText(), pwd));
            utils.showSuccess(errorLabel, "User added!");
        } else {
            utils.showError(errorLabel, "Please fill all the fields");
        }
    }

}
