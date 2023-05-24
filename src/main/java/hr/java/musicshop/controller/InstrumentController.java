package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.Instrument;
import hr.java.musicshop.iznimke.InvalidInputType;
import hr.java.musicshop.iznimke.NoMatchException;
import hr.java.musicshop.iznimke.NumberMismatch;
import hr.java.musicshop.sucelja.CheckForExceptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class InstrumentController<T extends Instrument> implements CheckForExceptions {

    private List<String> productDetails = new ArrayList<>();

    protected Comparator<String> priceComparator =
            (String v1, String v2) -> {
                if(Integer.parseInt(v1.substring(0, v1.length()-1)) >= Integer.parseInt(v2.substring(0, v2.length()-1)))
                    return 1;
                return 0;
            };

    protected List<T> filterNumbers(TextField priceFrom, TextField priceTo, List<T> filtrirana, String message){
        try{
            inputType(priceFrom);
            inputType(priceTo);
            numberMismatch(priceFrom, priceTo);
            if(!priceFrom.getText().isBlank()){
                inputType(priceFrom);
                filtrirana = filtrirana.stream().filter(s -> s.getCijena() >= Integer.parseInt(priceFrom.getText())).toList();
            }
            if(!priceTo.getText().isBlank()){
                inputType(priceTo);
                filtrirana = filtrirana.stream().filter(s -> s.getCijena() <= Integer.parseInt(priceTo.getText())).toList();
            }
            return filtrirana;
        }catch (NumberMismatch e){
            logger.error("Number mismatch exception");
            showAlert(message);
            return filtrirana;
        }catch(InvalidInputType e){
            logger.error(e.getMessage() + " must be numeric value!");
            showAlert(e.getMessage() + " must be numeric value!");
            return filtrirana;
        }
    }

    protected boolean checkInputType(TextField num) {
        try{
            inputType(num);
            return true;
        }catch (InvalidInputType e){
            logger.error(e.getMessage() + " must be numeric value!");
            showAlert(e.getMessage() + " must be numeric value!");
            return false;
        }
    }

    protected void initializeTableView(TableColumn<T,String> manufacturerColumn, TableColumn<T, String> priceColumn, TableView<T> tableView, List<T> lista){
        manufacturerColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getProizvodac() + " " + s.getValue().getNaziv()));
        priceColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getCijena() + "€"));
        priceColumn.setComparator(priceComparator);

        tableView.setItems(FXCollections.observableList(lista));
    }

    protected void setTableView(TableView<T> tableView, List<T> lista){
        tableView.setItems(FXCollections.observableList(lista));
        try{
            checkListSize(lista.size());
        }catch (NoMatchException e){
            logger.error("No instruments matching set filters.");
            tableView.setPlaceholder(new Label("No instruments matching set filters."));
        }
    }

    public void filter() throws SQLException, IOException {}

    public void reset(){}

    @FXML
    private void enter(KeyEvent k) throws SQLException, IOException {
        if(k.getCode() == KeyCode.ENTER)
            filter();
        if(k.getCode() == KeyCode.F1)
            reset();
    }

    protected void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS1.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Warning!");
        alert.setHeaderText("Invalid input!");
        alert.setContentText(message);

        alert.showAndWait();
    }

    protected void noProductSelectedAlert(String e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS1.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Warning!");
        alert.setHeaderText(e);
        alert.setContentText("Please select one product.");
        alert.showAndWait();
    }

    public void showProductPage(List<String> lista) throws IOException {
        FileWriter writer = new FileWriter("dat/productDetails",false);
        BufferedWriter out = new BufferedWriter(writer);
        for(String s:lista){
            out.write(s + "\n");
        }
        out.close();
        HelloApplication.showStage("productPage.fxml", "Product page", 700, 700);
    }
}
