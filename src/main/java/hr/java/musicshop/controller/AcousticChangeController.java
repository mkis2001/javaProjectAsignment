package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.*;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class AcousticChangeController extends BuilderController implements LoadFile {
    @FXML
    private CheckBox lineOut;
    private boolean lineOutUvjet = false;
    private static final String SLIKA_LINK = "https://img.freepik.com/premium-vector/acoustic-quitar-silhouette-musical-instrument-vector_591091-492.jpg";

    AkusticnaGitara gitara;

    public void initialize(){
        gitara = (AkusticnaGitara) getSelectedGuitar();
        shape.getItems().addAll("Dreadnought", "Jumbo", "Folk");
        body.getItems().addAll("Alder", "Basswood", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)", "Ash(+" + Wood.ASH.getPrice() + "€)");
        neck.getItems().addAll("Maple", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)", "Mahogany (+" + Wood.MAHOGANY.getPrice() + "€)");
        fretboard.getItems().addAll("Maple", "Rosewood (+" + Wood.ROSEWOOD.getPrice() + "€)", "Ebony (+" + Wood.EBONY.getPrice() + "€)", "Roasted maple (+" + Wood.ROASTED_MAPLE.getPrice() + "€)");
        shape.setValue(gitara.getShape().getNaziv());
        body.setValue(gitara.getBody().getNazivSaCijenom());
        neck.setValue(gitara.getNeck().getNazivSaCijenom());
        fretboard.setValue(gitara.getFretboard().getNazivSaCijenom());

        scale.getItems().addAll("24.75\"", "25\"" , "25.50\"");
        if (Double.parseDouble(gitara.getScaleLenght().toString()) == 24.75) {
            scale.setValue("24.75\"");
        } else if (Double.parseDouble(gitara.getScaleLenght().toString()) == 25.5) {
            scale.setValue("25.50\"");
        } else if (Integer.parseInt(gitara.getScaleLenght().toString()) == 25) {
            scale.setValue("25\"");
        }

        frets.getItems().addAll("20", "21", "22");
        frets.setValue(String.valueOf(gitara.getBrPragova()));

        finish.getItems().addAll("Natural", "Solid", "Translucent (+" + FinishType.TRANSLUCENT.getPrice() + "€)", "Sparkle (+" + FinishType.SPARKLE.getPrice() + "€)");
        finish.setValue(gitara.getFinish().getNazivSaCijenom());

        color.setText(gitara.getColor());

        lineOut.setSelected(gitara.getLineOut().equals(true));

        Thread cijena = new Thread(
                () -> {
                    while(HelloApplication.getStageName().equals("acousticChange.fxml")){
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
                    while(HelloApplication.getStageName().equals("acousticChange.fxml")){
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

        if(lineOut.isSelected()) {
            currentPrice += 100;
            lineOutUvjet = true;
        }else{
            lineOutUvjet = false;
        }

        return currentPrice;
    }

    public void confirmChanges() throws IOException, ClassNotFoundException {
        String shapeKratica = null;
        switch (shape.getValue()){
            case "Dreadnought" -> shapeKratica = AcousticShape.DREADNOUGHT.getKratica();
            case "Jumbo" -> shapeKratica = AcousticShape.JUMBO.getKratica();
            case "Folk" -> shapeKratica = AcousticShape.FOLK.getKratica();
        }
        AkusticnaGitara novaGitara;
        if(scale.getValue().length() < 4) {
            novaGitara = new AkusticnaGitara(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "AC Custom " + shapeKratica, priceCheck(), getSelectedFinish(), color.getText(), 6, Double.parseDouble(scale.getValue().substring(0, 2)), Integer.parseInt(frets.getValue()), getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), lineOutUvjet);
        }else{
            novaGitara = new AkusticnaGitara(Long.parseLong("0"), SLIKA_LINK, getCurrentUser(), "AC Custom " + shapeKratica, priceCheck(), getSelectedFinish(), color.getText(), 6, Double.parseDouble(scale.getValue().substring(0, 5)), Integer.parseInt(frets.getValue()), getWoodValue(body.getValue()), getWoodValue(neck.getValue()), getWoodValue(fretboard.getValue()), shape.getValue(), lineOutUvjet);

        }
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
            if (novaGitara.getBrPragova() != gitara.getBrPragova())
                changes = changes + "\nFRETS[" + gitara.getBrPragova() + " -> " + novaGitara.getBrPragova() + "]";
            if (!novaGitara.getFinish().equals(gitara.getFinish()))
                changes = changes + "\nFINISH[" + gitara.getFinish().getNaziv() + " -> " + novaGitara.getFinish().getNaziv() + "]";
            if (!novaGitara.getColor().equals(gitara.getColor()))
                changes = changes + "\nCOLOR[" + gitara.getColor() + " -> " + novaGitara.getColor() + "]";
            if(!novaGitara.getLineOut().equals(gitara.getLineOut()))
                changes = changes + "\nLINE OUT[" + gitara.getLineOut() + " -> " + novaGitara.getLineOut() + "]";

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
                    BazaPodataka.changeAcousticGuitar(gitara.getId(), novaGitara, changes);
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
        HelloApplication.showStage("acGuitars.fxml", "Acoustic Guitars", 600, 700);
    }
}
