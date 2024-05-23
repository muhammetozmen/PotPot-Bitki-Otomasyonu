package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class pageMainController {

    @FXML
    private Label label_test;

    @FXML
    public void initialize() {
    	System.out.println("✔️ pageMainController başlatıldı");
    }

    public String getLabelTest() {
        return label_test.getText();
    }

    public void setLabelTest(String text) {
        Platform.runLater(() -> {
            label_test.setText(text);
        });
    }
}


