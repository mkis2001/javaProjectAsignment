package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.AkusticniBubnjevi;
import hr.java.musicshop.entitet.Bass;
import hr.java.musicshop.entitet.DrumPart;
import hr.java.musicshop.iznimke.NoProductSelectedException;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class AcDrumsController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<AkusticniBubnjevi, String> productColumn;
    @FXML
    private TableColumn<AkusticniBubnjevi, String> cijenaColumn;
    @FXML
    private TableView<AkusticniBubnjevi> acDrumsTableView;
    @FXML
    private TextField manufacturerText;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private ChoiceBox<String> snareSize;
    @FXML
    private ChoiceBox<String> kickSize;
    @FXML
    private ChoiceBox<String> noToms;
    @FXML
    private ChoiceBox<String> noCymbals;

    private List<AkusticniBubnjevi> bubnjevi;

    public void initialize() throws SQLException, IOException {
        bubnjevi = BazaPodataka.getAkBubnjeve();

        snareSize.getItems().add("Pick");
        kickSize.getItems().add("Pick");
        noToms.getItems().add("Pick");
        noCymbals.getItems().add("Pick");
        for(AkusticniBubnjevi b:bubnjevi){
            if(!snareSize.getItems().contains(b.getSnareSize()))
                snareSize.getItems().add((String) b.getSnareSize());
            if(!kickSize.getItems().contains(b.getKickSize()))
                kickSize.getItems().add((String) b.getKickSize());
            if(!noToms.getItems().contains(String.valueOf(b.getDrumPart(DrumPart.TOM) + b.getDrumPart(DrumPart.FLOORTOM))))
                noToms.getItems().add(String.valueOf(b.getDrumPart(DrumPart.TOM) + b.getDrumPart(DrumPart.FLOORTOM)));
            if(!noCymbals.getItems().contains(String.valueOf(b.getDrumPart(DrumPart.CRASH) + b.getDrumPart(DrumPart.RIDE) + b.getDrumPart(DrumPart.HIHAT))))
                noCymbals.getItems().add(String.valueOf(b.getDrumPart(DrumPart.CRASH) + b.getDrumPart(DrumPart.RIDE) + b.getDrumPart(DrumPart.HIHAT)));
        }
        snareSize.setValue("Pick");
        kickSize.setValue("Pick");
        noToms.setValue("Pick");
        noCymbals.setValue("Pick");

        initializeTableView(productColumn, cijenaColumn, acDrumsTableView, bubnjevi);
    }

    public void filter(){
        List<AkusticniBubnjevi> filtrirana = bubnjevi;

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }
        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");
        if(!snareSize.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getSnareSize().equals(snareSize.getValue())).toList();
        if(!kickSize.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getKickSize().equals(kickSize.getValue())).toList();
        if(!noToms.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> String.valueOf(s.getDrumPart(DrumPart.TOM) + s.getDrumPart(DrumPart.FLOORTOM)).equals(noToms.getValue())).toList();
        if(!noCymbals.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> String.valueOf(s.getDrumPart(DrumPart.CRASH) + s.getDrumPart(DrumPart.RIDE) + s.getDrumPart(DrumPart.HIHAT)).equals(noCymbals.getValue())).toList();

        setTableView(acDrumsTableView, filtrirana);

    }

    public void reset(){
        snareSize.setValue("Pick");
        kickSize.setValue("Pick");
        noToms.setValue("Pick");
        noCymbals.setValue("Pick");

        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");

        setTableView(acDrumsTableView, bubnjevi);
    }

    public void selectItem() throws IOException {
        AkusticniBubnjevi inst = acDrumsTableView.getSelectionModel().getSelectedItem();
        try {
            checkSelectedProduct(inst);
            List<String> details = new ArrayList<>();
            details.add("ID:\n" + inst.getId());
            details.add("Manufacturer:\n" + inst.getProizvodac());
            details.add("Model:\n" + inst.getNaziv());
            details.add("Price:\n" + inst.getCijena() + "€");
            details.add("Type:\n" + inst.getTipInstrumenta().getNaziv());
            details.add("Finish:\n" + inst.getFinish().getNaziv());
            details.add("Color:\n" + inst.getColor());
            details.add("Snare size:\n" + inst.getSnareSize());
            details.add("Kick size:\n" + inst.getKickSize());
            details.add("Number of kicks:\n" + inst.getDrumPart(DrumPart.KICK));
            details.add("Number of rack toms:\n" + inst.getDrumPart(DrumPart.TOM));
            details.add("Number of floor toms:\n" + inst.getDrumPart(DrumPart.FLOORTOM));
            details.add("Number of hi-hats:\n" + inst.getDrumPart(DrumPart.HIHAT));
            details.add("Number of rides:\n" + inst.getDrumPart(DrumPart.RIDE));
            details.add("Number of crashes:\n" + inst.getDrumPart(DrumPart.CRASH));
            details.add("acDrums\n");
            details.add(inst.getSlikaLink());
            showProductPage(details);
        } catch (NoProductSelectedException | NullPointerException e) {
            logger.error(e.getMessage());
            noProductSelectedAlert(e.getMessage());
        }
    }

    public void backButton() throws IOException {
        HelloApplication.showStage("drums.fxml", "Drums",600,400);
    }
}
