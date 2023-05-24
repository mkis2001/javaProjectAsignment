package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.FinishType;
import hr.java.musicshop.entitet.Wood;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class BuilderController {
    @FXML
    protected ChoiceBox<String> shape;
    @FXML
    protected ChoiceBox<String> body;
    @FXML
    protected ChoiceBox<String> neck;
    @FXML
    protected ChoiceBox<String> fretboard;
    @FXML
    protected ChoiceBox<String> strings;
    @FXML
    protected ChoiceBox<String> scale;
    @FXML
    protected ChoiceBox<String> frets;
    @FXML
    protected ChoiceBox<String> finish;
    @FXML
    protected TextField color;
    @FXML
    protected Text price;
    protected static final int BASE_PRICE = 1750;
    String getWoodValue(String s){
        if(s.contains("Maple"))
            return "Maple";
        else if(s.contains("Alder"))
            return "Alder";
        else if(s.contains("Mahogany"))
            return "Mahogany";
        else if(s.contains("Rosewood"))
            return "Rosewood";
        else if(s.contains("Ebony"))
            return "Ebony";
        else if(s.contains("Roasted"))
            return "Roasted maple";
        else if(s.contains("Ash"))
            return "Ash";
        else
            return "Basswood";
    }

    protected int woodPriceCheck(){
        int currentPrice = BASE_PRICE;
        if(body.getValue().contains("Mahogany"))
            currentPrice += Wood.MAHOGANY.getPrice();
        else if(body.getValue().contains("Ash"))
            currentPrice += Wood.ASH.getPrice();

        if(neck.getValue().contains("Mahogany"))
            currentPrice += Wood.MAHOGANY.getPrice();
        else if(neck.getValue().contains("Roasted"))
            currentPrice += Wood.ROASTED_MAPLE.getPrice();

        if(fretboard.getValue().contains("Roasted"))
            currentPrice += Wood.ROASTED_MAPLE.getPrice();
        else if(fretboard.getValue().contains("Ebony"))
            currentPrice += Wood.EBONY.getPrice();
        else if(fretboard.getValue().contains("Rosewood"))
            currentPrice += Wood.ROSEWOOD.getPrice();

        if(finish.getValue().contains("Translucent"))
            currentPrice += FinishType.TRANSLUCENT.getPrice();
        else if(finish.getValue().contains("Sparkle"))
            currentPrice += FinishType.SPARKLE.getPrice();

        return currentPrice;
    }

    protected void buildProhibitedAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS1.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Warning!");
        alert.setHeaderText("New builds prohibitet at the moment.");
        alert.setContentText("Wait for the current build to finish.");

        alert.showAndWait();
    }

    protected String getSelectedFinish(){
        String finishType;
        if (finish.getValue().contains("Natural"))
            finishType = "Natural";
        else if (finish.getValue().contains("Solid"))
            finishType = "Solid";
        else if (finish.getValue().contains("Translucent"))
            finishType = "Translucent";
        else
            finishType = "Sparkle";

        return finishType;
    }

    public void backButton() throws IOException {
        HelloApplication.showStage("zicani.fxml", "String instruments",600,400);
    }
}
