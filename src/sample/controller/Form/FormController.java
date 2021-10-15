package sample.controller.Form;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Admin
 */
public class FormController implements Initializable {

    @FXML
    private VBox vbox;
    private Parent fxml;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../fxml/Form/SignUp.fxml")));
            vbox.getChildren().removeAll();
            vbox.getChildren().setAll(fxml);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        open_signin();
    }

    @FXML
    private void open_signin(){
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.5), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../fxml/Form/SignIn.fxml")));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void open_signup(){
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.5), vbox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../../fxml/Form/SignUp.fxml")));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.close();
        System.exit(-1);
    }
}

