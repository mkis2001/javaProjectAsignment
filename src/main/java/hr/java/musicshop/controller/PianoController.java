package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.DrumPart;
import hr.java.musicshop.entitet.ElektricniBubnjevi;
import hr.java.musicshop.entitet.Piano;
import hr.java.musicshop.iznimke.InvalidInputType;
import hr.java.musicshop.iznimke.NoProductSelectedException;
import hr.java.musicshop.iznimke.NumberMismatch;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class PianoController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<Piano, String> productColumn;
    @FXML
    private TableColumn<Piano, String> cijenaColumn;
    @FXML
    private TableView<Piano> pianoTableView;
    @FXML
    private TextField manufacturerText;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private ChoiceBox<String> color;
    @FXML
    private TextField weightFrom;
    @FXML
    private TextField weightTo;
    @FXML
    private TextField heightFrom;
    @FXML
    private TextField heightTo;

    List<Piano> pianos = new ArrayList<>();

    public void initialize() throws SQLException, IOException {
        pianos = BazaPodataka.getPianos();
        List<Integer> visineTezine = tezineVisine(pianos);

        color.getItems().add("Pick");
        for(Piano p:pianos){
            if(!color.getItems().contains(p.getColor()))
                color.getItems().add(p.getColor());
        }

        color.setValue("Pick");

        weightFrom.setPromptText("From (min: " + visineTezine.get(0) + "kg)");
        weightTo.setPromptText("To (max: " + visineTezine.get(1) + "kg)");
        heightFrom.setPromptText("From (min: " + visineTezine.get(2) + "cm)");
        heightTo.setPromptText("To (max: " + visineTezine.get(3) + "cm)");

        initializeTableView(productColumn, cijenaColumn, pianoTableView, pianos);
    }

    public void filter(){
        List<Piano> filtrirana = pianos;

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }
        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");

        if(!color.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getColor().equals(color.getValue())).toList();

        try{
            inputType(weightFrom);
            inputType(weightTo);
            numberMismatch(weightFrom, weightTo);
            if(!weightFrom.getText().isBlank()){
                inputType(weightFrom);
                filtrirana = filtrirana.stream().filter(s -> s.getWeight() >= Integer.parseInt(weightFrom.getText())).toList();
            }
            if(!weightTo.getText().isBlank()){
                inputType(weightTo);
                filtrirana = filtrirana.stream().filter(s -> s.getWeight() <= Integer.parseInt(weightTo.getText())).toList();
            }
        }catch (NumberMismatch e){
            logger.error(e.getMessage());
            showAlert("Minimum weight can't be bigger than maximum weight.");
        }catch(InvalidInputType e){
            logger.error(e.getMessage());
            showAlert(e.getMessage() + " must be numeric value!");
        }

        try{
            inputType(heightFrom);
            inputType(heightTo);
            numberMismatch(heightFrom, heightTo);
            if(!heightFrom.getText().isBlank()){
                inputType(heightFrom);
                filtrirana = filtrirana.stream().filter(s -> s.getHeight() >= Integer.parseInt(heightFrom.getText())).toList();
            }
            if(!heightTo.getText().isBlank()){
                inputType(heightTo);
                filtrirana = filtrirana.stream().filter(s -> s.getHeight() <= Integer.parseInt(heightTo.getText())).toList();
            }
        }catch (NumberMismatch e){
            logger.error(e.getMessage());
            showAlert("Minimum height can't be higher than maximum height.");
        }catch(InvalidInputType e){
            logger.error(e.getMessage());
            showAlert(e.getMessage() + " must be numeric value!");
        }

        setTableView(pianoTableView, filtrirana);
    }

    public void reset(){
        color.setValue("Pick");
        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");
        weightFrom.setText("");
        weightTo.setText("");
        heightFrom.setText("");
        heightTo.setText("");

        setTableView(pianoTableView, pianos);
    }

    private List<Integer> tezineVisine(List<Piano> p){
        List<Integer> tezine = new ArrayList<>();
        List<Integer> visine = new ArrayList<>();
        for(Piano pi:p){
            tezine.add(pi.getWeight());
            visine.add(pi.getHeight());
        }
        List<Integer> lista = new ArrayList<>();
        lista.add(Collections.min(tezine));
        lista.add(Collections.max(tezine));
        lista.add(Collections.min(visine));
        lista.add(Collections.max(visine));

        return lista;
    }

    public void selectItem() throws IOException {
        Piano inst = pianoTableView.getSelectionModel().getSelectedItem();
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
            details.add("Weight:\n" + inst.getWeight()+"kg");
            details.add("Height:\n" + inst.getHeight()+"cm");
            details.add("piano\n");
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
