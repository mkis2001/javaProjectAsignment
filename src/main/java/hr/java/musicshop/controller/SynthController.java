package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.Bass;
import hr.java.musicshop.entitet.Piano;
import hr.java.musicshop.entitet.Synth;
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

public class SynthController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<Synth, String> productColumn;
    @FXML
    private TableColumn<Synth, String> cijenaColumn;
    @FXML
    private TableView<Synth> synthTableView;
    @FXML
    private TextField manufacturerText;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private ChoiceBox<String> keyNumber;
    @FXML
    private ChoiceBox<String> modulationWheel;
    @FXML
    private ChoiceBox<String> arpeg;

    List<Synth> synths = new ArrayList<>();

    public void initialize() throws SQLException, IOException {
        synths = BazaPodataka.getSynths();

        keyNumber.getItems().add("Pick");
        modulationWheel.getItems().addAll("Pick", "Yes", "No");
        arpeg.getItems().addAll("Pick", "Yes", "No");

        for(Synth s:synths){
            if(!keyNumber.getItems().contains(String.valueOf(s.getKeyNumber())))
                keyNumber.getItems().add(String.valueOf(s.getKeyNumber()));
        }

        keyNumber.setValue("Pick");
        modulationWheel.setValue("Pick");
        arpeg.setValue("Pick");

        initializeTableView(productColumn, cijenaColumn, synthTableView, synths);
    }

    public void filter(){
        List<Synth> filtrirana = synths;

        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }

        if(!keyNumber.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getKeyNumber() == Integer.parseInt(keyNumber.getValue())).toList();

        if(!modulationWheel.getValue().equals("Pick")){
            if(modulationWheel.getValue().equals("Yes")) {
                filtrirana = filtrirana.stream().filter(Synth::isModulationWheel).toList();
            }else if(modulationWheel.getValue().equals("No"))
                filtrirana = filtrirana.stream().filter(s -> !s.isModulationWheel()).toList();
        }
        if(!arpeg.getValue().equals("Pick")){
            if(arpeg.getValue().equals("Yes"))
                filtrirana = filtrirana.stream().filter(Synth::isArpeggiator).toList();
            else if(arpeg.getValue().equals("No"))
                filtrirana = filtrirana.stream().filter(s -> !s.isArpeggiator()).toList();
        }

        setTableView(synthTableView, filtrirana);
    }

    public void reset() {
        keyNumber.setValue("Pick");
        modulationWheel.setValue("Pick");
        arpeg.setValue("Pick");

        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");

        setTableView(synthTableView, synths);
    }

    public void selectItem() throws IOException {
        Synth inst = synthTableView.getSelectionModel().getSelectedItem();
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
            details.add("Number of keys:\n" + inst.getKeyNumber());
            if(inst.isModulationWheel())
                details.add("Modulation wheel:\nYes");
            else
                details.add("Modulation wheel:\nNo");
            if(inst.isArpeggiator())
                details.add("Arpeggiator:\nYes");
            else
                details.add("Arpeggiator:\nNo");
            details.add("synth\n");
            details.add(inst.getSlikaLink());
            showProductPage(details);
        } catch (NoProductSelectedException | NullPointerException e) {
            logger.error(e.getMessage());
            noProductSelectedAlert(e.getMessage());
        }
    }

    public void backButton() throws IOException {
        HelloApplication.showStage("keys.fxml", "Keys", 600, 400);
    }
}
