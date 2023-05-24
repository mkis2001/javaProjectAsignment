package hr.java.musicshop.niti;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.controller.HelloApplication;
import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.entitet.ElektricnaGitara;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Objects;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class GuitarBuilderNit implements Runnable, LoadFile {
    int price;
    int brojac;
    String user;
    ElektricnaGitara gitara;

    public GuitarBuilderNit(int price, int brojac, ElektricnaGitara gitara) {
        this.price = price;
        this.brojac = brojac;
        this.gitara = gitara;
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
                        BazaPodataka.addCustomGuitar(gitara);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
                        dialogPane.getStyleClass().add("informationDialog");
                        alert.setTitle("Build completed");
                        alert.setHeaderText(null);
                        alert.setContentText("Guitar finished building.");

                        alert.showAndWait();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    try {
                        Dogadaj d = new Dogadaj("Guitar '" + gitara.getProizvodac() + " " + gitara.getNaziv() +"' created by '" + user + "'");
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
