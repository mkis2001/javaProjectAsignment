package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.*;
import hr.java.musicshop.niti.AcousticBuilderNit;
import hr.java.musicshop.niti.BassBuilderNit;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class BassBuilderController extends BuilderController implements LoadFile {
    @FXML
    private ChoiceBox<String> pickups;
    @FXML
    private CheckBox preamp;
    private boolean preampUvjet = false;
    private int brZica = 4;
    private static final String SLIKA_LINK = "https://www.pngitem.com/pimgs/m/211-2119287_bass-guitar-silhouette-png-transparent-png.png";

    public void initialize(){
        shape.getItems().addAll("Jazz", "P shape", "Alternative");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue("Jazz");
        body.setValue("Alder");
        neck.setValue("Maple");
        fretboard.setValue("Maple");

        strings.getItems().addAll("4", "5 (+70€)", "6 (+150€)");
        strings.setValue("4");

        scale.getItems().addAll("32\"", "34\"", "35\"");
        scale.setValue("34\"");

        pickups.getItems().addAll("S-S", "H-H", "H", "H-S");
        pickups.setValue("S-S");

        frets.getItems().addAll("20", "21", "22", "24", "Fretless (+100€)");
        frets.setValue("20");

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue("Natural");

        preamp.setSelected(false);
        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("bassBuilder.fxml")){
                        try {
                            price.setText(priceCheck() + "€");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        Thread checkFinish = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("bassBuilder.fxml")){
                        try{
                            if(finish.getValue().equals("Natural")){
                                color.setText("No color");
                                color.setEditable(false);
                                color.setDisable(true);
                            }else{
                                color.setEditable(true);
                                color.setDisable(false);
                            }
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        cijena.start();
        checkFinish.start();
    }

    private int priceCheck(){
        int price = woodPriceCheck();

        if(frets.getValue().contains("Fretless"))
            price += 100;

        if(preamp.isSelected()) {
            price += 100;
            preampUvjet = true;
            Platform.runLater(
                    () -> {
                        preamp.setText("Active");
                    }
            );
        }else{
            preampUvjet = false;
            Platform.runLater(
                    () -> {
                        preamp.setText("Passive");
                    }
            );
        }

        if(strings.getValue().contains("5 (+70€)")) {
            price += 70;
            brZica = 5;
        }
        else if(strings.getValue().contains("6 (+150€)")) {
            price += 150;
            brZica = 6;
        }else{
            brZica = 4;
        }

        return price;
    }

    public void build(){
        if(HelloApplication.getMainStage().getTitle().contains("Build progress:")){
            buildProhibitedAlert();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Confirmation");
            alert.setHeaderText("Start build");
            alert.setContentText("Are you sure you want to build this bass?");
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes) {
                String shapeKratica = null;
                switch (shape.getValue()){
                    case "Jazz" -> shapeKratica = BassShape.JAZZ.getKratica();
                    case "P shape" -> shapeKratica = BassShape.P.getKratica();
                    case "Alternative" -> shapeKratica = BassShape.ALTERNATIVE.getKratica();
                }

                int brPragova;
                if(frets.getValue().contains("Fretless")){
                    brPragova = 0;
                }else
                    brPragova = Integer.parseInt(frets.getValue().substring(0, 2));

                Bass bass = new Bass(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "BCustom " + shapeKratica + " " + brZica, priceCheck(), getSelectedFinish(), color.getText(), brZica, Double.parseDouble(scale.getValue().substring(0,2)), brPragova, getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), pickups.getValue(), preampUvjet);
                Thread builderNit = new Thread(new BassBuilderNit(priceCheck(), 0, bass));
                builderNit.start();
            }
        }
    }
}
