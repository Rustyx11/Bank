package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class mainController {
    @FXML
    private Label welcomeText,accout_holder_label;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
