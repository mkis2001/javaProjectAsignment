package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class ProductPageController implements LoadFile {
    @FXML
    private TextFlow savTekst;
    @FXML
    private ImageView slika;
    @FXML
    private Text nazivText;
    @FXML
    private Text cijenaText;

    private String path;

    List<String> details;
    Long id;
    String naziv;
    String cijena;

    public void initialize(){
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("dat/productDetails"));
            details = bufReader.lines().toList();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        for(int i=8;i<details.size()-3;i+=2){
            Text t = new Text(details.get(i) + " ");
            t.setStyle("-fx-fill: #125679;-fx-font-weight: bold;");
            Text t2 = new Text(details.get(i+1) + "\n");
            t2.setStyle("-fx-fill: #125679;");
            savTekst.getChildren().add(t);
            savTekst.getChildren().add(t2);
        }

        id = Long.valueOf(details.get(1));
        naziv = details.get(3) + " " + details.get(5);
        cijena = details.get(7);
        path = details.get(details.size()-3);

        nazivText.setText(naziv);
        cijenaText.setText(cijena);

        slika.setImage(new Image(details.get(details.size()-1)));
    }

    public void backButton() throws IOException {
        if(path.equals("elGuitars"))
            HelloApplication.showStage("elGuitars.fxml", "Electric Guitars", 600, 700);
        if(path.equals("acGuitars"))
            HelloApplication.showStage("acGuitars.fxml", "Acoustic Guitars", 600, 700);
        if(path.equals("bass"))
            HelloApplication.showStage("bass.fxml", "Bass Guitars", 600,700);
        if(path.equals("acDrums"))
            HelloApplication.showStage("acDrums.fxml", "Acoustic Drums", 600, 700);
        if(path.equals("elDrums"))
            HelloApplication.showStage("elDrums.fxml", "Electric Drums", 600, 700);
        if(path.equals("piano"))
            HelloApplication.showStage("piano.fxml", "Pianos", 600,700);
        if(path.equals("synth"))
            HelloApplication.showStage("synth.fxml", "Synthesizers", 600, 700);
    }

    public void purchase() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
        dialogPane.getStyleClass().add("informationDialog");
        dialogPane.setMinWidth(400);
        dialogPane.setMinHeight(200);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Purchase");
        alert.setContentText("Are you sure you want to purchase '" + naziv + "' for " + cijena + "?");
        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes) {
            BazaPodataka.deleteInstrument(path, id, naziv, cijena);
            dodajDogadaj(new Dogadaj("'" + naziv + "' purchased for " + cijena));
            alert = new Alert(Alert.AlertType.INFORMATION);
            dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssInfoDialog.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            dialogPane.setMinWidth(400);
            alert.setTitle("Guitar purchased");
            alert.setHeaderText(null);
            alert.setContentText("'" + naziv + "' purchased for " + cijena + ".");

            alert.showAndWait();
            backButton();
        }
    }
}
