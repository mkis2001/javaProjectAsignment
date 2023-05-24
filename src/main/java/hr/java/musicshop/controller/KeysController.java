package hr.java.musicshop.controller;

import java.io.IOException;

public class KeysController {
    public void showPianoScreen() throws IOException {
        HelloApplication.showStage("piano.fxml", "Pianos", 600, 700);
    }

    public void showSynthScreen() throws IOException{
        HelloApplication.showStage("synth.fxml", "Synthesizers", 600, 700);
    }
}
