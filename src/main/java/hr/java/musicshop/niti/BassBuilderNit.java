package hr.java.musicshop.niti;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.controller.HelloApplication;
import hr.java.musicshop.entitet.Bass;
import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Objects;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class BassBuilderNit implements Runnable, LoadFile {
    int price;
    int brojac;
    Bass bass;
    String user;

    public BassBuilderNit(int price, int brojac, Bass bass) {
        this.price = price;
        this.brojac = brojac;
        this.bass = bass;
        user = getCurrentUser();
    }

    @Override
    public void run() {
        while(brojac < price){
            try {
                Platform.runLater(
                        () -> {
                            HelloApplication.getMainStage().setTitle(HelloApplication.getStageTitle() + " | Build progress: " + (int)((double)brojac/price*100) + "%" );
                        }
                );
                System.out.println((int)((double)brojac/price*100) + "%");
                brojac += 100;
                if(brojac>price){
                    brojac=price;
                }
                Thread.sleep(500);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(
                () -> {
                    try {
                        BazaPodataka.addCustomBass(bass);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
                        dialogPane.getStyleClass().add("informationDialog");
                        alert.setTitle("Build completed");
                        alert.setHeaderText(null);
                        alert.setContentText("Bass finished building.");

                        alert.showAndWait();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    try {
                        Dogadaj d = new Dogadaj("Bass '" + bass.getProizvodac() + " " + bass.getNaziv() +"' created by '" + user + "'");
                        d.setUser(user);
                        dodajDogadaj(d);
                    } catch (IOException | ClassNotFoundException e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    HelloApplication.getMainStage().setTitle(HelloApplication.getStageTitle());
                }
        );
    }
}
