package sample.controller.utils;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DropDown {
    private Stage stage;
    private VBox dropdown;
    private StackPane stackWindow;

    public DropDown(Stage stage, StackPane stackWindow) {
        this.stage = stage;
        this.stackWindow = stackWindow;
        createNewDropDown();
    }

    private void createNewDropDown() {
        dropdown = new VBox();
        dropdown.setStyle("-fx-background-color: #2A2F32;");
        dropdown.setPrefWidth(250);
    }

    public void add(DropDownItem dropDownItem) {
        dropdown.getChildren().add(dropDownItem);
    }

    public void show(double sceneX, double sceneY) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setOnMouseClicked(event -> {
            stackWindow.getChildren().remove(anchorPane);
        });
        anchorPane.getChildren().add(dropdown);
        dropdown.setLayoutX(sceneX);
        dropdown.setLayoutY(sceneY);
        stackWindow.getChildren().add(anchorPane);
    }
}
