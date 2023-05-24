package hr.java.musicshop.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class MenuButtonController {
    public void showHomePage() throws IOException {
        HelloApplication.showStage("hello-view.fxml", "Java Music Shop", 600, 300 );
    }

    public void esc(KeyEvent k) throws IOException {
        if(k.getCode() == KeyCode.ESCAPE)
            showHomePage();
    }
}
