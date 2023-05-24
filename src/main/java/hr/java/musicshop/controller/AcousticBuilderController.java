package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.*;
import hr.java.musicshop.niti.AcousticBuilderNit;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;


public class AcousticBuilderController extends BuilderController implements LoadFile {
    @FXML
    private CheckBox lineOut;
    private static final String SLIKA_LINK = "https://img.freepik.com/premium-vector/acoustic-quitar-silhouette-musical-instrument-vector_591091-492.jpg";


    public void initialize(){
        shape.getItems().addAll("Dreadnought", "Jumbo", "Folk");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue("Dreadnought");
        body.setValue("Alder");
        neck.setValue("Maple");
        fretboard.setValue("Maple");

        scale.getItems().addAll("24.75\"", "25\"" , "25.50\"");
        scale.setValue("25.50");

        frets.getItems().addAll("20", "21", "22");
        frets.setValue("20");

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue("Natural");

        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("acousticBuilder.fxml")){
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
                    while(HelloApplication.getStageName().equals("acousticBuilder.fxml")){
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

    public synchronized void build(){
        if(HelloApplication.getMainStage().getTitle().contains("Build progress:")){
            buildProhibitedAlert();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Confirmation");
            alert.setHeaderText("Start build");
            alert.setContentText("Are you sure you want to build this guitar?");
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes) {
                String shapeKratica = null;
                switch (shape.getValue()){
                    case "Dreadnought" -> shapeKratica = AcousticShape.DREADNOUGHT.getKratica();
                    case "Jumbo" -> shapeKratica = AcousticShape.JUMBO.getKratica();
                    case "Folk" -> shapeKratica = AcousticShape.FOLK.getKratica();
                }
                boolean lineOutUvjet;
                if(lineOut.isSelected())
                    lineOutUvjet = true;
                else
                    lineOutUvjet = false;

                AkusticnaGitara gitara;
                if(scale.getValue().length() < 4){
                    gitara = new AkusticnaGitara(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "AC Custom " + shapeKratica, priceCheck(), getSelectedFinish(), color.getText(), 6, Double.parseDouble(scale.getValue().substring(0, 2)), Integer.parseInt(frets.getValue()), getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), lineOutUvjet);
                }else {
                    gitara = new AkusticnaGitara(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "AC Custom " + shapeKratica, priceCheck(), getSelectedFinish(), color.getText(), 6, Double.parseDouble(scale.getValue().substring(0, 5)), Integer.parseInt(frets.getValue()), getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), lineOutUvjet);
                }
                Thread builderNit = new Thread(new AcousticBuilderNit(priceCheck(), 0, gitara));
                builderNit.start();
            }
        }
    }

    private synchronized int priceCheck(){
        int currentPrice = woodPriceCheck();

        if(lineOut.isSelected()) {
            currentPrice += 100;
        }

        return currentPrice;
    }
}
