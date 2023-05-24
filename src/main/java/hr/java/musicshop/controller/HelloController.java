package hr.java.musicshop.controller;

import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HelloController implements LoadFile {
    @FXML
    private Button button;

    public void initialize(){
        if(!getCurrentUser().equals("java"))
            button.setVisible(false);
        else
            button.setVisible(true);
    }

    public void showLoginScreen() throws IOException {
        HelloApplication.showStage("login.fxml", "Login", 400, 400);
    }
    public void showZicaniScreen() throws IOException {
        HelloApplication.showStage("zicani.fxml", "String instruments",600,400);
    }

    public void showDrumsScreen() throws IOException{
        HelloApplication.showStage("drums.fxml", "Drums", 600, 400 );
    }

    public void showKeysScreen() throws IOException{
        HelloApplication.showStage("keys.fxml", "Keys", 600, 400);
    }

    public void showEventsScreen() throws IOException{
        HelloApplication.showStage("dogadaji.fxml", "Events", 800, 700);
    }
}