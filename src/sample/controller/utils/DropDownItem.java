package sample.controller.utils;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class DropDownItem extends HBox {
    private String name;
    private EventHandler eventHandler;

    public DropDownItem (String name, EventHandler eventHandler) {
        this.name = name;
        this.eventHandler = eventHandler;
        getChildren().add(createDropDownItem());;
    }

    private Label createDropDownItem() {
        Label label = new Label(name);
        label.setTextFill(Paint.valueOf("white"));
        label.setFont(Font.font("Calibri italic", 19));
        label.setOnMouseClicked(event -> {
            this.eventHandler.handle();
        });
        label.setPrefWidth(250);
        label.setPadding(new Insets(20));
        getStyleClass().add("dropdown-item");
        setCursor(Cursor.HAND);
        return label;
    }
}
