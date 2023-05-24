package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.*;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class GuitarChangeController extends BuilderController implements LoadFile {
    @FXML
    private ChoiceBox<String> pickups;
    @FXML
    private ChoiceBox<String> bridge;
    ElektricnaGitara gitara;

    public void initialize() throws IOException {
        gitara = (ElektricnaGitara) getSelectedGuitar();
        shape.getItems().addAll("Stratocaster", "Telecaster", "Single cut", "Offset", "Alternative");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue(gitara.getShape().getNaziv());
        body.setValue(gitara.getBody().getNazivSaCijenom());
        neck.setValue(gitara.getNeck().getNazivSaCijenom());
        fretboard.setValue(gitara.getFretboard().getNazivSaCijenom());

        strings.getItems().addAll("6", "7 (+70€)", "8 (+150€)");
        switch(gitara.getBrojZica()){
            case 6 -> strings.setValue("6");
            case 7 -> strings.setValue("7 (+70€)");
            case 8 -> strings.setValue("8 (+150€)");
        }

        scale.getItems().addAll("24.75\"", "25.50\"", "26.50\" (+50€)", "27\" (+80€)", "28.50\" (+150€)");
        if (Double.parseDouble(gitara.getScaleLenght().toString()) == 24.75) {
            scale.setValue("24.75\"");
        } else if (Double.parseDouble(gitara.getScaleLenght().toString()) == 25.5) {
            scale.setValue("25.50\"");
        } else if (Double.parseDouble(gitara.getScaleLenght().toString()) == 26.5) {
            scale.setValue("26.50\" (+50€)");
        } else if (Double.parseDouble(gitara.getScaleLenght().toString()) == 27) {
            scale.setValue("27\" (+80€)");
        } else if (Double.parseDouble(gitara.getScaleLenght().toString()) == 28.5) {
            scale.setValue("28.50\" (+150€)");
        }

        pickups.getItems().addAll("H-H", "S-S-S", "H-S-S", "P90-P90");
        pickups.setValue(gitara.getPickupConfig());

        frets.getItems().addAll("21", "22", "24");
        frets.setValue(String.valueOf(gitara.getBrPragova()));

        bridge.getItems().addAll("Tune-O-Matic", "Hardtail", "Tremolo (+" + Bridge.TREMOLO.getPrice() + "€)", "Floyd Rose (+" + Bridge.FLOYD.getPrice() + "€)");
        bridge.setValue(gitara.getBridge().getNazivSaCijenom());

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue(gitara.getFinish().getNazivSaCijenom());

        color.setText(gitara.getColor());

        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("guitarChange.fxml")){
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
                    while(HelloApplication.getStageName().equals("guitarChange.fxml")){
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
        int price = BASE_PRICE;
        if(body.getValue().contains("Mahogany"))
            price += Wood.MAHOGANY.getPrice();
        else if(body.getValue().contains("Ash"))
            price += Wood.ASH.getPrice();

        if(neck.getValue().contains("Mahogany"))
            price += Wood.MAHOGANY.getPrice();
        else if(neck.getValue().contains("Roasted"))
            price += Wood.ROASTED_MAPLE.getPrice();

        if(fretboard.getValue().contains("Roasted"))
            price += Wood.ROASTED_MAPLE.getPrice();
        else if(fretboard.getValue().contains("Ebony"))
            price += Wood.EBONY.getPrice();
        else if(fretboard.getValue().contains("Rosewood"))
            price += Wood.ROSEWOOD.getPrice();

        if(strings.getValue().contains("7"))
            price += 70;
        else if(strings.getValue().contains("8"))
            price += 150;

        if(scale.getValue().contains("26.5"))
            price += 50;
        else if(scale.getValue().contains("27"))
            price += 80;
        else if(scale.getValue().contains("28.5"))
            price += 150;

        if(bridge.getValue().contains("Tremolo"))
            price += Bridge.TREMOLO.getPrice();
        else if(bridge.getValue().contains("Floyd"))
            price += Bridge.FLOYD.getPrice();

        return price;
    }

    public void confirmChanges() throws IOException, ClassNotFoundException {
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
        String finishType;
        if (finish.getValue().contains("Natural"))
            finishType = "Natural";
        else if (finish.getValue().contains("Solid"))
            finishType = "Solid";
        else if (finish.getValue().contains("Translucent"))
            finishType = "Translucent";
        else
            finishType = "Sparkle";

        double scaleLenght;
        if(scale.getValue().charAt(2) != '.'){
            scaleLenght = Double.parseDouble(scale.getValue().substring(0, 2));
        }else{
            scaleLenght = Double.parseDouble(scale.getValue().substring(0, 5));
        }

        ElektricnaGitara novaGitara = new ElektricnaGitara.Builder(0)
                .setSlikaLink("https://www.pngkey.com/png/detail/75-758748_black-guitar-silhouette-electric-guitar-silhouette-png.png")
                .setProizvodac(gitara.getProizvodac())
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

        if (novaGitara.equals(gitara)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("There are no changes made to this guitar.");

            alert.showAndWait();
        } else {
            String changes = "Changes:";
            if (!novaGitara.getShape().equals(gitara.getShape()))
                changes = changes + "\nSHAPE[" + gitara.getShape().getNaziv() + " -> " + novaGitara.getShape().getNaziv() + "]";

            if (!novaGitara.getBody().equals(gitara.getBody()))
                changes = changes + "\nBODY[" + gitara.getBody().getNaziv() + " -> " + novaGitara.getBody().getNaziv() + "]";

            if (!novaGitara.getNeck().equals(gitara.getNeck()))
                changes = changes + "\nNECK[" + gitara.getNeck().getNaziv() + " -> " + novaGitara.getNeck().getNaziv() + "]";

            if (!novaGitara.getFretboard().equals(gitara.getFretboard()))
                changes = changes + "\nFRETBOARD[" + gitara.getFretboard().getNaziv() + " -> " + novaGitara.getFretboard().getNaziv() + "]";

            if (novaGitara.getBrojZica() != gitara.getBrojZica())
                changes = changes + "\nSTRINGS[" + gitara.getBrojZica() + " -> " + novaGitara.getBrojZica() + "]";
            if (!novaGitara.getScaleLenght().equals(Double.parseDouble(gitara.getScaleLenght().toString())))
                changes = changes + "\nSCALE LENGHT[" + gitara.getScaleLenght() + " -> " + novaGitara.getScaleLenght() + "]";
            if (!novaGitara.getPickupConfig().equals(gitara.getPickupConfig()))
                changes = changes + "\nPICKUPS[" + gitara.getPickupConfig() + " -> " + novaGitara.getPickupConfig() + "]";
            if (novaGitara.getBrPragova() != gitara.getBrPragova())
                changes = changes + "\nFRETS[" + gitara.getBrPragova() + " -> " + novaGitara.getBrPragova() + "]";
            if (!novaGitara.getBridge().equals(gitara.getBridge()))
                changes = changes + "\nBRIDGE[" + gitara.getBridge().getNaziv() + " -> " + novaGitara.getBridge().getNaziv() + "]";
            if (!novaGitara.getFinish().equals(gitara.getFinish()))
                changes = changes + "\nFINISH[" + gitara.getFinish().getNaziv() + " -> " + novaGitara.getFinish().getNaziv() + "]";
            if (!novaGitara.getColor().equals(gitara.getColor()))
                changes = changes + "\nCOLOR[" + gitara.getColor() + " -> " + novaGitara.getColor() + "]";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm changes");
            alert.setContentText("Are you sure you want to make following changes: " + changes);
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes) {
                if (changes.equals("Changes:")) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSS1.css")).toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("There are no changes made to this instrument.");

                    alert.showAndWait();
                } else {
                    BazaPodataka.changeGuitar(gitara.getId(), novaGitara, changes);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
                    dialogPane.getStyleClass().add("informationDialog");
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Update completed");

                    alert.showAndWait();
                    dodajDogadaj(new Dogadaj("Guitar edited: " + changes));
                    backButton();
                }
            }
        }

    }

    @Override
    public void backButton() throws IOException {
        HelloApplication.showStage("elGuitars.fxml", "Electric Guitars", 600, 700);
    }
}
