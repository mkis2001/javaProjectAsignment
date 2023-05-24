package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.AkusticnaGitara;
import hr.java.musicshop.entitet.Bass;
import hr.java.musicshop.entitet.ElektricnaGitara;
import hr.java.musicshop.iznimke.NoProductSelectedException;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class BassController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<Bass, String> productColumn;
    @FXML
    private TableColumn<Bass, String> cijenaColumn;
    @FXML
    private TableView<Bass> bassTableView;
    @FXML
    private TextField manufacturerText;
    @FXML
    private TextField priceFrom;
    @FXML
    private TextField priceTo;
    @FXML
    private ChoiceBox<String> stringNumber;
    @FXML
    private ChoiceBox<String> scaleLenght;
    @FXML
    private ChoiceBox<String> pickupConfig;
    @FXML
    private ChoiceBox<String> noFrets;
    @FXML
    private ChoiceBox<String> preamp;
    @FXML
    private Button button;
    private List<Bass> bass = new ArrayList<>();

    public void initialize() throws SQLException, IOException {
        bass = BazaPodataka.getBassGitare();

        stringNumber.getItems().add("Pick");
        pickupConfig.getItems().add("Pick");
        noFrets.getItems().add("Pick");
        preamp.getItems().addAll("Pick", "Passive", "Active");
        for (Bass b : bass) {
            if (!stringNumber.getItems().contains(String.valueOf(b.getBrojZica())))
                stringNumber.getItems().add(String.valueOf(b.getBrojZica()));
            if (!noFrets.getItems().contains(String.valueOf(b.getBrPragova())))
                noFrets.getItems().add(String.valueOf(b.getBrPragova()));
            if (!pickupConfig.getItems().contains(b.getPickupConfig()))
                pickupConfig.getItems().add(b.getPickupConfig());
            if(!scaleLenght.getItems().contains(String.valueOf(b.getScaleLenght()) + '"'))
                scaleLenght.getItems().add(String.valueOf(b.getScaleLenght()) + '"');
        }

        stringNumber.setValue("Pick");
        scaleLenght.setValue("Pick");
        pickupConfig.setValue("Pick");
        noFrets.setValue("Pick");
        preamp.setValue("Pick");

        initializeTableView(productColumn, cijenaColumn, bassTableView, bass);

        Thread checkButton = new Thread(
                () -> {
                    String user = getCurrentUser();
                    while (HelloApplication.getStageName().equals("bass.fxml")) {
                        try {
                            Bass gitara = bassTableView.getSelectionModel().getSelectedItem();
                            if (gitara != null && gitara.getNaziv().contains("Custom")) {
                                if (user.equals("java"))
                                    button.setVisible(true);
                                else if (gitara.getProizvodac().equals(user))
                                    button.setVisible(true);
                                else
                                    button.setVisible(false);
                            } else
                                button.setVisible(false);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        checkButton.start();
    }

    public void filter(){
        List<Bass> filtrirana = bass;

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }
        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");
        if(!stringNumber.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getBrojZica() == Integer.parseInt(stringNumber.getValue())).toList();
        if(!noFrets.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> String.valueOf(s.getBrPragova()).equals(noFrets.getValue())).toList();
        if(!scaleLenght.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> Double.parseDouble(s.getScaleLenght().toString()) == Double.parseDouble(scaleLenght.getValue().substring(0,scaleLenght.getValue().length()-1))).toList();
        if(!pickupConfig.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getPickupConfig().equals(pickupConfig.getValue())).toList();
        if(!preamp.getValue().equals("Pick")) {
            if (preamp.getValue().equals("Active"))
                filtrirana = filtrirana.stream().filter(s -> s.getPreamp().equals(Boolean.TRUE)).toList();
            if (preamp.getValue().equals("Passive"))
                filtrirana = filtrirana.stream().filter(s -> s.getPreamp().equals(Boolean.FALSE)).toList();
        }

        setTableView(bassTableView,filtrirana);


    }

    public void reset(){
        stringNumber.setValue("Pick");
        pickupConfig.setValue("Pick");
        noFrets.setValue("Pick");
        preamp.setValue("Pick");
        scaleLenght.setValue("Pick");

        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");

        setTableView(bassTableView, bass);
    }

    public void selectItem() throws IOException {
        Bass inst = bassTableView.getSelectionModel().getSelectedItem();
        try {
            checkSelectedProduct(inst);
            List<String> details = new ArrayList<>();
            details.add("ID:\n" + inst.getId());
            details.add("Manufacturer:\n" + inst.getProizvodac());
            details.add("Model:\n" + inst.getNaziv());
            details.add("Price:\n" + inst.getCijena() + "€");
            details.add("Type:\n" + inst.getTipInstrumenta().getNaziv());
            details.add("Shape:\n" + inst.getShape().getNaziv());
            details.add("Finish:\n" + inst.getFinish().getNaziv());
            details.add("Color:\n" + inst.getColor());
            details.add("Number of strings:\n" + inst.getBrojZica());
            details.add("Scale lenght:\n" + inst.getScaleLenght() + '"');
            details.add("Number of frets:\n" + inst.getBrPragova());
            details.add("Body wood:\n" + inst.getBody().getNaziv());
            details.add("Neck wood:\n" + inst.getNeck().getNaziv());
            details.add("Fretboard wood:\n" + inst.getFretboard().getNaziv());
            details.add("Pickup configuration:\n" + inst.getPickupConfig());
            if (inst.getPreamp()) {
                details.add("Preamp:\nActive");
            } else
                details.add("Preamp:\nPassive");
            details.add("bass\n");
            details.add(inst.getSlikaLink());
            showProductPage(details);
        } catch (NoProductSelectedException | NullPointerException e) {
            logger.error(e.getMessage());
            noProductSelectedAlert(e.getMessage());
        }
    }

    public void editInstrument(){
        Bass gitara = bassTableView.getSelectionModel().getSelectedItem();
        if(gitara.getProizvodac().equals(getCurrentUser()) || getCurrentUser().equals("java")) {
            try {
                checkSelectedProduct(gitara);
                addGuitar(gitara.getId(), "Bass");
                HelloApplication.showStage("bassChange.fxml", "Bass editor", 600, 700);
            } catch (NoProductSelectedException | IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void backButton() throws IOException {
        HelloApplication.showStage("zicani.fxml", "String instruments",600,400);
    }
}
