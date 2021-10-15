package sample.utils;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public String hashPassword(String s) {
        return BCrypt.hashpw(s, BCrypt.gensalt(12));
    }

    public void showError(Label label, String err) {
        label.setText(err);
        label.setVisible(true);
        setVisibleAnimation(label, 2);
    }

    public void showSuccess(Label label, String err) {
        label.setText(err);
        label.setStyle("-fx-text-fill: #00ff00;");
        label.setVisible(true);
        setVisibleAnimation(label, 2);
    }

    private void setVisibleAnimation(Label label, int sec) {
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(sec)
        );
        visiblePause.setOnFinished(
                event -> label.setVisible(false)
        );
        visiblePause.play();
    }

    public void setProfile(String path, Circle avatar) {
        Image img = new Image(path);
        avatar.setFill(new ImagePattern(img));
    }

    public String getCurrTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String getCurrDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public boolean isNumberOnly(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public void slideAnimation(double v, Pane pane) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.4), pane);
        t.setToX(v);
        t.play();
    }

    public void showNotFoundErr(VBox vbox, Label label) {
        vbox.getChildren().clear();
        vbox.getChildren().add(label);
    }
}
