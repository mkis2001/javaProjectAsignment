package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.sucelja.HashAndCheckPass;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public final class SignupController implements LoadFile, HashAndCheckPass {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPass;

    public void initialize(){
        Platform.runLater(()->username.requestFocus());
        currentUser("none");
    }

    public void signUp() throws IOException {
        boolean uvjet = true;
        if(username.getText().isBlank() || password.getText().isBlank() || confirmPass.getText().isBlank()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("All fields must be filled");
            alert.setContentText(null);

            alert.showAndWait();
            uvjet = false;
        }
        if(uvjet && !password.getText().equals(confirmPass.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Field 'Confirm password' must be the same as 'Password'!");
            alert.setContentText(null);

            alert.showAndWait();
            uvjet = false;
        }
        if(uvjet){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssInfoDialog.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add new user");
            alert.setContentText("Are you sure you want to add user '" + username.getText() + "'");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                addUser(username.getText(), hashPassword(password.getText()));
                HelloApplication.showStage("login.fxml", "Login", 400, 400);
            } else {
                initialize();
            }
        }
    }

    @FXML
    private void enter(KeyEvent k) throws IOException {
        if(k.getCode() == KeyCode.ENTER)
            signUp();
    }

}
