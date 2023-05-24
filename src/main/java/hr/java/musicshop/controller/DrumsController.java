package hr.java.musicshop.controller;

import java.io.IOException;

public class DrumsController {
    public void showAcDrumsScreen() throws IOException {
        HelloApplication.showStage("acDrums.fxml", "Acoustic Drums", 600, 700);
    }

    public void showElDrumsScreen() throws IOException {
        HelloApplication.showStage("elDrums.fxml", "Electric Drums", 600, 700);
    }
}
