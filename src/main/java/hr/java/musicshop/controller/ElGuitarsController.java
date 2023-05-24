package hr.java.musicshop.controller;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.entitet.ElektricnaGitara;
import hr.java.musicshop.iznimke.NoProductSelectedException;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class ElGuitarsController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<ElektricnaGitara, String> productColumn;
    @FXML
    private TableColumn<ElektricnaGitara, String> cijenaColumn;
    @FXML
    private TableView<ElektricnaGitara> elGitareTableView;
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
    private ChoiceBox<String> bridge;
    @FXML
    private ChoiceBox<String> noFrets;
    private List<ElektricnaGitara> elGitare = new ArrayList<>();

    @FXML
    private Button button;

    public void initialize(){
        elGitare = BazaPodataka.getElGitare();

        stringNumber.getItems().add("Pick");
        scaleLenght.getItems().add("Pick");
        pickupConfig.getItems().add("Pick");
        bridge.getItems().add("Pick");
        noFrets.getItems().add("Pick");
        for(ElektricnaGitara e:elGitare){
            if(!stringNumber.getItems().contains(String.valueOf(e.getBrojZica())))
                stringNumber.getItems().add(String.valueOf(e.getBrojZica()));
            if(!scaleLenght.getItems().contains(String.valueOf(e.getScaleLenght()) + '"'))
                scaleLenght.getItems().add(String.valueOf(e.getScaleLenght()) + '"');
            if(!pickupConfig.getItems().contains(e.getPickupConfig()))
                pickupConfig.getItems().add(e.getPickupConfig());
            if(!bridge.getItems().contains(e.getBridge().getNaziv()))
                bridge.getItems().add(e.getBridge().getNaziv());
            if(!noFrets.getItems().contains(String.valueOf(e.getBrPragova())))
                noFrets.getItems().add(String.valueOf(e.getBrPragova()));
        }
        stringNumber.setValue("Pick");
        scaleLenght.setValue("Pick");
        pickupConfig.setValue("Pick");
        bridge.setValue("Pick");
        noFrets.setValue("Pick");
        initializeTableView(productColumn, cijenaColumn, elGitareTableView, elGitare);

        button.setVisible(false);

        Thread checkButton = new Thread(
                () -> {
                    String user = getCurrentUser();
                    while (HelloApplication.getStageName().equals("elGuitars.fxml")) {
                        try {
                            ElektricnaGitara gitara = elGitareTableView.getSelectionModel().getSelectedItem();
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

    public void filter() {
        List<ElektricnaGitara> filtrirana = elGitare;

        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }

        if(!stringNumber.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getBrojZica() == Integer.parseInt(stringNumber.getValue())).toList();
        if(!scaleLenght.getValue().equals("Pick")){
            filtrirana = filtrirana.stream().filter(s -> Double.parseDouble(s.getScaleLenght().toString()) == Double.parseDouble(scaleLenght.getValue().substring(0,scaleLenght.getValue().length()-1))).toList();
        }
        if(!pickupConfig.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getPickupConfig().equals(pickupConfig.getValue())).toList();
        if(!bridge.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getBridge().getNaziv().equals(bridge.getValue())).toList();
        if(!noFrets.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> String.valueOf(s.getBrPragova()).equals(noFrets.getValue())).toList();
        setTableView(elGitareTableView, filtrirana);
    }

    public void reset(){
        stringNumber.setValue("Pick");
        scaleLenght.setValue("Pick");
        pickupConfig.setValue("Pick");
        bridge.setValue("Pick");
        noFrets.setValue("Pick");

        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");

        setTableView(elGitareTableView, BazaPodataka.getElGitare());
    }

    public void selectItem() throws IOException {
        ElektricnaGitara gitara = elGitareTableView.getSelectionModel().getSelectedItem();
        try{
            checkSelectedProduct(gitara);
            List<String> details = new ArrayList<>();
            details.add("ID:\n" + gitara.getId());
            details.add("Manufacturer:\n" + gitara.getProizvodac());
            details.add("Model:\n" + gitara.getNaziv());
            details.add("Price:\n" + gitara.getCijena() + "€");
            details.add("Type:\n" + gitara.getTipInstrumenta().getNaziv());
            details.add("Shape:\n" + gitara.getShape().getNaziv());
            details.add("Finish:\n" + gitara.getFinish().getNaziv());
            details.add("Color:\n" + gitara.getColor());
            details.add("Number of strings:\n" + gitara.getBrojZica());
            details.add("Scale lenght:\n" + gitara.getScaleLenght() + '"');
            details.add("Number of frets:\n" + gitara.getBrPragova());
            details.add("Body wood:\n" + gitara.getBody().getNaziv());
            details.add("Neck wood:\n" + gitara.getNeck().getNaziv());
            details.add("Fretboard wood:\n" + gitara.getFretboard().getNaziv());
            details.add("Pickup configuration:\n" + gitara.getPickupConfig());
            details.add("Bridge:\n" + gitara.getBridge().getNaziv());
            details.add("elGuitars\n");
            details.add(gitara.getSlikaLink());
            showProductPage(details);
        }catch (NoProductSelectedException | NullPointerException e){
            logger.error(e.getMessage());
            noProductSelectedAlert(e.getMessage());
        }
    }

    public void editInstrument(){
        ElektricnaGitara gitara = elGitareTableView.getSelectionModel().getSelectedItem();
        if(gitara.getProizvodac().equals(getCurrentUser()) || getCurrentUser().equals("java")) {
            try {
                checkSelectedProduct(gitara);
                addGuitar(gitara.getId(), "ElektricnaGitara");
                HelloApplication.showStage("guitarChange.fxml", "Guitar editor", 600, 700);
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
