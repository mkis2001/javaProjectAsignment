package hr.java.musicshop.controller;

import java.io.IOException;

public class ZicaniController {

    public void showElectricGuitarScreen() throws IOException {
        HelloApplication.showStage("elGuitars.fxml", "Electric Guitars", 600, 700);
    }

    public void showGuitarBuilderScreen() throws IOException{
        HelloApplication.showStage("guitarBuilder.fxml", "Electric guitars Custom Shop", 600, 700);
    }

    public void showAcousticGuitarScreen() throws IOException {
        HelloApplication.showStage("acGuitars.fxml", "Acoustic Guitars", 600, 700);
    }

    public void showAcousticBuilderScreen() throws IOException{
        HelloApplication.showStage("acousticBuilder.fxml", "Acoustic guitars Custom Shop", 600, 700);
    }

    public void showBassGuitarScreen() throws IOException{
        HelloApplication.showStage("bass.fxml", "Bass Guitars", 600, 700);
    }

    public void showBassBuilderScreen() throws IOException{
        HelloApplication.showStage("bassBuilder.fxml", "Bass guitars Custom Shop", 600, 700);
    }
}
