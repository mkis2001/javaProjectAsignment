package hr.java.musicshop.controller;

import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.iznimke.IncorrectPasswordException;
import hr.java.musicshop.sucelja.CheckForExceptions;
import hr.java.musicshop.sucelja.HashAndCheckPass;
import hr.java.musicshop.sucelja.LoadFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static hr.java.musicshop.controller.HelloApplication.logger;

public non-sealed class LoginController implements LoadFile, HashAndCheckPass {

    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private Map<String, String> users;

    public void initialize() throws IOException, ClassNotFoundException {
        Platform.runLater(()->username.requestFocus());
        currentUser("none");
        users = getUsers();
    }

    public void login() throws IOException, ClassNotFoundException {
        try{
            checkPassword(username.getText(), password.getText(), users);
            currentUser(username.getText());
            dodajDogadaj(new Dogadaj("User logged in"));
            HelloApplication.showStage("hello-view.fxml", "Home Screen", 600, 300);
        }catch(IncorrectPasswordException e){
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSS1.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Warning");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Type in correct username and password in order to log in.");

            alert.showAndWait();
        }
    }

    public void signUp() throws IOException {
        HelloApplication.showStage("signup.fxml", "Sign Up", 400, 400);
    }

    public void delete() throws IOException, ClassNotFoundException {
        if(username.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSS1.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Warning");
            alert.setHeaderText("No account name!");
            alert.setContentText("You must type in a name of a account you want to delete.");

            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSSINFODIALOG.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete user");
            alert.setContentText("Are you sure you want to delete user '" + username.getText() + "'");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteUser(username.getText());
                users = getUsers();
            } else {
                initialize();
            }
        }
    }

    public void showUsers(){
        String usersString = "Users:";
        for(String s:users.keySet()){
            usersString = usersString + "\n" + s;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
        dialogPane.getStyleClass().add("informationDialog");
        dialogPane.setMinHeight(250);
        alert.setTitle("Users");
        alert.setHeaderText(null);
        alert.setContentText(usersString);

        alert.showAndWait();
    }

    @FXML
    private void enter(KeyEvent k) throws IOException, ClassNotFoundException {
        if(k.getCode() == KeyCode.ENTER)
            login();
    }

}
