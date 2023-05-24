package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.entitet.ElektricnaGitara;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DogadajiController implements LoadFile {
    @FXML
    private TableColumn<Dogadaj, String> nazivColumn;
    @FXML
    private TableColumn<Dogadaj, String> vrijemeColumn;
    @FXML
    private TableColumn<Dogadaj, String> usersColumn;
    @FXML
    private TableView<Dogadaj> dogadajTableView;
    @FXML
    private ChoiceBox<String> user;

    private List<Dogadaj> dogadaji;

    public void initialize() throws IOException, ClassNotFoundException {
        dogadaji = getDogadaji();
        Collections.reverse(dogadaji);

        Map<String, String> users = getUsers();

        for(String u:users.keySet()){
                user.getItems().add(u);
        }
        user.setValue("Pick");
        nazivColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getNaziv()));
        vrijemeColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))));
        usersColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getUser()));
        dogadajTableView.setItems(FXCollections.observableList(dogadaji));

    }

    public void filter(){
        List<Dogadaj> filtrirana = dogadaji;

        if(!user.getValue().equals("Pick")) {
            filtrirana = filtrirana.stream().filter(s -> s.getUser().equals(user.getValue())).toList();
        }

        dogadajTableView.setItems(FXCollections.observableList(filtrirana));
    }

    public void reset() throws IOException, ClassNotFoundException {
        user.setValue("Pick");
        dogadajTableView.setItems(FXCollections.observableList(dogadaji));
    }

    public void details(){
        Dogadaj dogadaj = dogadajTableView.getSelectionModel().getSelectedItem();
        if(dogadaj != null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            dialogPane.setMinHeight(250);
            alert.setTitle("Event details");
            alert.setHeaderText(null);
            alert.setContentText("User: " + dogadaj.getUser() + "\n\nDescription: " + dogadaj.getNaziv() + "\n\nDate: " + dogadaj.getVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));

            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("CSS1.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Warning!");
            alert.setContentText("Please select one event.");
            alert.showAndWait();
        }
    }

    public void soldInstruments(){
        Set<String> set = BazaPodataka.getSoldInstruments();
        String tekst = "List of sold instruments:";

        for(String s:set){
            tekst = tekst + "\n" + s;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
        dialogPane.getStyleClass().add("informationDialog");
        alert.setTitle("Sold instruments");
        TextArea area = new TextArea(tekst);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        alert.showAndWait();
    }



}
