package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.*;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class BassChangeController extends BuilderController implements LoadFile {
    @FXML
    private ChoiceBox<String> pickups;
    @FXML
    private CheckBox preamp;
    private boolean preampUvjet = false;
    private int brZica = 4;
    private static final String SLIKA_LINK = "https://www.pngitem.com/pimgs/m/211-2119287_bass-guitar-silhouette-png-transparent-png.png";

    Bass bass;

    public void initialize(){
        bass = (Bass) getSelectedGuitar();
        shape.getItems().addAll("Jazz", "P shape", "Alternative");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue(bass.getShape().getNaziv());
        body.setValue(bass.getBody().getNazivSaCijenom());
        neck.setValue(bass.getNeck().getNazivSaCijenom());
        fretboard.setValue(bass.getFretboard().getNazivSaCijenom());

        strings.getItems().addAll("4", "5 (+70€)", "6 (+150€)");
        switch(bass.getBrojZica()){
            case 4 -> strings.setValue("4");
            case 5 -> strings.setValue("5 (+70€)");
            case 6 -> strings.setValue("6 (+150€)");
        }

        scale.getItems().addAll("32\"", "34\"", "35\"");
        scale.setValue(bass.getScaleLenght() + "\"");

        pickups.getItems().addAll("S-S", "H-H", "H", "H-S");
        pickups.setValue(bass.getPickupConfig());

        frets.getItems().addAll("20", "21", "22", "24", "Fretless (+100€)");
        if(bass.getBrPragova() == 0){
            frets.setValue("Fretless (+100€)");
        }else
            frets.setValue(String.valueOf(bass.getBrPragova()));

        if(bass.getPreamp()) {
            preamp.setSelected(true);
            preampUvjet=true;
        }
        else {
            preamp.setSelected(false);
        }

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue(bass.getFinish().getNazivSaCijenom());

        color.setText(bass.getColor());

        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("bassChange.fxml")){
                        try {
                            price.setText(priceCheck() + "€");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        Thread checkFinish = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("bassChange.fxml")){
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

        if(strings.getValue().equals("5 (+70€)")) {
            price += 70;
            brZica = 5;
        }
        else if(strings.getValue().equals("6 (+150€)")) {
            price += 150;
            brZica = 6;
        }else{
            brZica = 4;
        }

        return price;
    }

    public void confirmChanges() throws IOException, ClassNotFoundException {

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

        Bass noviBass = new Bass(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "BCustom " + shapeKratica + " " + brZica, priceCheck(), getSelectedFinish(), color.getText(), brZica, Double.parseDouble(scale.getValue().substring(0,2)), brPragova, getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), pickups.getValue(), preampUvjet);

        String changes = "Changes:";
        if (!noviBass.getShape().equals(bass.getShape()))
            changes = changes + "\nSHAPE[" + bass.getShape().getNaziv() + " -> " + noviBass.getShape().getNaziv() + "]";
        if (!noviBass.getBody().equals(bass.getBody()))
            changes = changes + "\nBODY[" + bass.getBody().getNaziv() + " -> " + noviBass.getBody().getNaziv() + "]";
        if (!noviBass.getNeck().equals(bass.getNeck()))
            changes = changes + "\nNECK[" + bass.getNeck().getNaziv() + " -> " + noviBass.getNeck().getNaziv() + "]";
        if (!noviBass.getFretboard().equals(bass.getFretboard()))
            changes = changes + "\nFRETBOARD[" + bass.getFretboard().getNaziv() + " -> " + noviBass.getFretboard().getNaziv() + "]";
        if (noviBass.getBrojZica() != bass.getBrojZica())
            changes = changes + "\nSTRINGS[" + bass.getBrojZica() + " -> " + noviBass.getBrojZica() + "]";
        if (!noviBass.getScaleLenght().equals(Double.parseDouble(bass.getScaleLenght().toString())))
            changes = changes + "\nSCALE LENGHT[" + bass.getScaleLenght() + " -> " + noviBass.getScaleLenght() + "]";
        if (!noviBass.getPickupConfig().equals(bass.getPickupConfig()))
            changes = changes + "\nPICKUPS[" + bass.getPickupConfig() + " -> " + noviBass.getPickupConfig() + "]";
        if (noviBass.getBrPragova() != bass.getBrPragova())
            changes = changes + "\nFRETS[" + bass.getBrPragova() + " -> " + noviBass.getBrPragova() + "]";
        if(!noviBass.getPreamp().equals(bass.getPreamp()))
            changes = changes + "\nPREAMP[" + bass.getPreamp() + " -> " + noviBass.getPreamp() + "]";
        if (!noviBass.getFinish().equals(bass.getFinish()))
            changes = changes + "\nFINISH[" + bass.getFinish().getNaziv() + " -> " + noviBass.getFinish().getNaziv() + "]";
        if (!noviBass.getColor().equals(bass.getColor()))
            changes = changes + "\nCOLOR[" + bass.getColor() + " -> " + noviBass.getColor() + "]";

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
                BazaPodataka.changeBass(bass.getId(), noviBass, changes);
                System.out.println(noviBass.getNaziv());
                alert = new Alert(Alert.AlertType.INFORMATION);
                dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
                dialogPane.getStyleClass().add("informationDialog");
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Update completed");

                alert.showAndWait();
                dodajDogadaj(new Dogadaj("Bass edited: " + changes));
                backButton();
            }
        }
    }

    @Override
    public void backButton() throws IOException {
        HelloApplication.showStage("bass.fxml", "Bass Guitars", 600, 700);
    }
}
