package hr.java.musicshop.controller;

import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.SQLException;

public class SearchBarController implements LoadFile {
    @FXML
    private TextField search;
    private String user;

    public void initialize(){
        user = getCurrentUser();
    }

    public void search() throws IOException {
        if(sadrzi("strings") || sadrzi("string") || sadrzi("guitars"))
            HelloApplication.showStage("zicani.fxml", "String instruments", 600, 400);
        else if(sadrzi("el guitars") || sadrzi("el guitar") || sadrzi("electric guitars") || sadrzi("electric guitar"))
            HelloApplication.showStage("elGuitars.fxml", "Electric Guitars", 600, 700);
        else if (sadrzi("custom") || sadrzi("custom shop") || sadrzi("build") || sadrzi("builder") || sadrzi("guitar builder") || sadrzi("electric guitar builder") || sadrzi("el guitar builder"))
            HelloApplication.showStage("guitarBuilder.fxml", "Guitar Custom Shop", 600, 700);
        else if(sadrzi("ac guitars") || sadrzi("ac guitar") || sadrzi("acoustic guitars") || sadrzi("acoustic guitar"))
            HelloApplication.showStage("acGuitars.fxml", "Acoustic Guitars", 600, 700);
        else if(sadrzi("bass") || sadrzi("el bass") || sadrzi("electric bass"))
            HelloApplication.showStage("bass.fxml", "Bass Guitars", 600, 700);
        else if(sadrzi("drums") || sadrzi("drum"))
            HelloApplication.showStage("drums.fxml", "Drums", 600, 400);
        else if (sadrzi("ac drums") || sadrzi("acoustic drums") || sadrzi("ac drum") || sadrzi("acoustic drum"))
            HelloApplication.showStage("acDrums.fxml", "Acoustic Drums", 600, 700);
        else if (sadrzi("el drums") || sadrzi("electric drums") || sadrzi("el drum") || sadrzi("electric drum"))
            HelloApplication.showStage("elDrums.fxml", "Electric Drums", 600, 700);
        else if(sadrzi("keys") || sadrzi("key"))
            HelloApplication.showStage("keys.fxml", "Keys", 600, 400);
        else if(sadrzi("pianos") || sadrzi("piano"))
            HelloApplication.showStage("piano.fxml", "Pianos", 600, 700);
        else if(sadrzi("synth") || sadrzi("synths") || sadrzi("synthesizers") || sadrzi("synthesizer"))
            HelloApplication.showStage("synth.fxml", "Synthesizers", 600, 700);
        else if(user.equals("java") && (sadrzi("event") || sadrzi("events")))
            HelloApplication.showStage("dogadaji.fxml", "Events", 800, 700);

    }

    private boolean sadrzi(String s){
        return search.getText().equalsIgnoreCase(s);
    }

    @FXML
    private void enter(KeyEvent k) throws IOException {
        if(search.focusedProperty().get()) {
            if (k.getCode() == KeyCode.ENTER)
                search();
        }
    }
}
