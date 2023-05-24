package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.Bridge;
import hr.java.musicshop.entitet.ElektricnaGitara;
import hr.java.musicshop.entitet.FinishType;
import hr.java.musicshop.entitet.Wood;
import hr.java.musicshop.niti.GuitarBuilderNit;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class GuitarBuilderController extends BuilderController implements LoadFile {
    @FXML
    private ChoiceBox<String> pickups;
    @FXML
    private ChoiceBox<String> bridge;
    private static final String SLIKA_LINK = "https://www.pngkey.com/png/detail/75-758748_black-guitar-silhouette-electric-guitar-silhouette-png.png";


    public void initialize(){
        shape.getItems().addAll("Stratocaster", "Telecaster", "Single cut", "Offset", "Alternative");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue("Stratocaster");
        body.setValue("Alder");
        neck.setValue("Maple");
        fretboard.setValue("Maple");

        strings.getItems().addAll("6", "7 (+70€)", "8 (+150€)");
        strings.setValue("6");

        scale.getItems().addAll("24.75\"", "25.50\"", "26.50\" (+50€)", "27\" (+80€)", "28.50\" (+150€)");
        scale.setValue("25.50\"");

        pickups.getItems().addAll("H-H", "S-S-S", "H-S-S", "P90-P90");
        pickups.setValue("H-H");

        frets.getItems().addAll("21", "22", "24");
        frets.setValue("21");

        bridge.getItems().addAll("Tune-O-Matic", "Hardtail", "Tremolo (+" + Bridge.TREMOLO.getPrice() + "€)", "Floyd Rose (+" + Bridge.FLOYD.getPrice() + "€)");
        bridge.setValue("Tune-O-Matic");

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue("Natural");

        price.setText(BASE_PRICE + "€");

        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("guitarBuilder.fxml")){
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
                    while(HelloApplication.getStageName().equals("guitarBuilder.fxml")){
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

    private synchronized int priceCheck(){
        int currentPrice = woodPriceCheck();
        if(strings.getValue().contains("7"))
            currentPrice += 70;
        else if(strings.getValue().contains("8"))
            currentPrice += 150;

        if(scale.getValue().contains("26.5"))
            currentPrice += 50;
        else if(scale.getValue().contains("27"))
            currentPrice += 80;
        else if(scale.getValue().contains("28.5"))
            currentPrice += 150;

        if(bridge.getValue().contains("Tremolo"))
            currentPrice += Bridge.TREMOLO.getPrice();
        else if(bridge.getValue().contains("Floyd"))
            currentPrice += Bridge.FLOYD.getPrice();

        return currentPrice;
    }

    public synchronized void build(){
        if(HelloApplication.getMainStage().getTitle().contains("Build progress:")){
            buildProhibitedAlert();
        }else {
            int bridgeInt;
            String brigdeKratica;
            if (bridge.getValue().contains("Tune")) {
                bridgeInt = 1;
                brigdeKratica = Bridge.TUNEOMATIC.getKratica();
            } else if (bridge.getValue().contains("Hard")) {
                bridgeInt = 2;
                brigdeKratica = Bridge.HARDTAIL.getKratica();
            } else if (bridge.getValue().contains("Tremolo")) {
                bridgeInt = 3;
                brigdeKratica = Bridge.TREMOLO.getKratica();
            } else {
                bridgeInt = 4;
                brigdeKratica = Bridge.FLOYD.getKratica();
            }
            String naziv = "Custom " + strings.getValue().charAt(0) + " " + brigdeKratica;
            String finishType = getSelectedFinish();

            double scaleLenght;

            if(scale.getValue().charAt(2) != '.'){
                scaleLenght = Double.parseDouble(scale.getValue().substring(0, 2));
            }else{
                scaleLenght = Double.parseDouble(scale.getValue().substring(0, 5));
            }

            ElektricnaGitara gitara = new ElektricnaGitara.Builder(0)
                    .setSlikaLink(SLIKA_LINK)
                    .setProizvodac(getCurrentUser())
                    .setNaziv(naziv)
                    .setCijena(priceCheck())
                    .setShape(shape.getValue())
                    .setFinish(finishType)
                    .setColor(color.getText())
                    .setBrZica(Integer.parseInt(strings.getValue().substring(0, 1)))
                    .setScaleLenght(scaleLenght)
                    .setBrPragova(Integer.parseInt(frets.getValue()))
                    .setBody(getWoodValue(body.getValue()))
                    .setNeck(getWoodValue(neck.getValue()))
                    .setFretboard(getWoodValue(fretboard.getValue()))
                    .setPickups(pickups.getValue())
                    .setBridge(bridgeInt)
                    .build();

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
                Thread builderNit = new Thread(new GuitarBuilderNit(priceCheck(), 0, gitara));
                builderNit.start();
            }
        }
    }
}
