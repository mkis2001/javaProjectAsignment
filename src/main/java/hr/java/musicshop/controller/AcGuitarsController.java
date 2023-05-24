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

public class AcGuitarsController extends InstrumentController implements LoadFile {
    @FXML
    private TableColumn<AkusticnaGitara, String> productColumn;
    @FXML
    private TableColumn<AkusticnaGitara, String> cijenaColumn;
    @FXML
    private TableView<AkusticnaGitara> acGitareTableView;
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
    private ChoiceBox<String> shape;
    @FXML
    private ChoiceBox<String> lineOut;
    @FXML
    private Button button;

    private List<AkusticnaGitara> aGitare = new ArrayList<>();

    public void initialize() throws SQLException, IOException {
        aGitare = BazaPodataka.getAkGitare();

        stringNumber.getItems().add("Pick");
        scaleLenght.getItems().add("Pick");
        shape.getItems().add("Pick");
        lineOut.getItems().addAll("Pick", "Yes", "No");
        for(AkusticnaGitara a:aGitare){
            if(!stringNumber.getItems().contains(String.valueOf(a.getBrojZica())))
                stringNumber.getItems().add(String.valueOf(a.getBrojZica()));
            if(!scaleLenght.getItems().contains(String.valueOf(a.getScaleLenght()) + '"'))
                scaleLenght.getItems().add(String.valueOf(a.getScaleLenght()) + '"');
            if(!shape.getItems().contains(a.getShape().toString()))
                shape.getItems().add(a.getShape().toString());
        }
        stringNumber.setValue("Pick");
        scaleLenght.setValue("Pick");
        shape.setValue("Pick");
        lineOut.setValue("Pick");

        initializeTableView(productColumn, cijenaColumn, acGitareTableView, aGitare);

        Thread checkButton = new Thread(
                () -> {
                    String user = getCurrentUser();
                    while (HelloApplication.getStageName().equals("acGuitars.fxml")) {
                        try {
                            AkusticnaGitara gitara = acGitareTableView.getSelectionModel().getSelectedItem();
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
        List<AkusticnaGitara> filtrirana = aGitare;

        if(!manufacturerText.getText().isBlank()){
            filtrirana = filtrirana.stream().filter(s -> s.getProizvodac().toLowerCase().contains(manufacturerText.getText().toLowerCase()) || s.getNaziv().toLowerCase().contains(manufacturerText.getText())).toList();
        }
        filtrirana = filterNumbers(priceFrom, priceTo, filtrirana, "Minimal price can't be higher than maximal price.");
        if(!stringNumber.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getBrojZica() == Integer.parseInt(stringNumber.getValue())).toList();
        if(!scaleLenght.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> Double.parseDouble(s.getScaleLenght().toString()) == Double.parseDouble(scaleLenght.getValue().substring(0,scaleLenght.getValue().length()-1))).toList();
        if(!shape.getValue().equals("Pick"))
            filtrirana = filtrirana.stream().filter(s -> s.getShape().toString().equals(shape.getValue())).toList();
        if(!lineOut.getValue().equals("Pick")){
            if(lineOut.getValue().equals("Yes"))
                filtrirana = filtrirana.stream().filter(s -> s.getLineOut().equals(Boolean.TRUE)).toList();
            else if(lineOut.getValue().equals("No"))
                filtrirana = filtrirana.stream().filter(s -> s.getLineOut().equals(Boolean.FALSE)).toList();
        }

        setTableView(acGitareTableView, filtrirana);
    }

    public void reset(){
        stringNumber.setValue("Pick");
        scaleLenght.setValue("Pick");
        shape.setValue("Pick");
        lineOut.setValue("Pick");

        manufacturerText.setText("");
        priceFrom.setText("");
        priceTo.setText("");

        setTableView(acGitareTableView, aGitare);
    }

    public void selectItem() throws IOException{
        AkusticnaGitara gitara = acGitareTableView.getSelectionModel().getSelectedItem();
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
            if(gitara.getLineOut()){
                details.add("Line out:\n" + "Yes");
            }else
                details.add("Line out:\n" + "No");
            details.add("acGuitars\n");
            details.add(gitara.getSlikaLink());
            showProductPage(details);
        }catch (NoProductSelectedException | NullPointerException e){
            logger.error(e.getMessage());
            noProductSelectedAlert(e.getMessage());
        }
    }

    public void editInstrument(){
        AkusticnaGitara gitara = acGitareTableView.getSelectionModel().getSelectedItem();
        if(gitara.getProizvodac().equals(getCurrentUser()) || getCurrentUser().equals("java")) {
            try {
                checkSelectedProduct(gitara);
                addGuitar(gitara.getId(), "AkusticnaGitara");
                HelloApplication.showStage("acousticChange.fxml", "Acoustic guitar editor", 600, 700);
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
