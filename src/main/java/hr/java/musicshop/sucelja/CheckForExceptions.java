package hr.java.musicshop.sucelja;

import hr.java.musicshop.controller.HelloApplication;
import hr.java.musicshop.controller.InstrumentController;
import hr.java.musicshop.controller.LoginController;
import hr.java.musicshop.controller.PianoController;
import hr.java.musicshop.entitet.Dogadaj;
import hr.java.musicshop.entitet.Instrument;
import hr.java.musicshop.iznimke.*;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Map;

public interface CheckForExceptions<T extends Instrument> {

    default void numberMismatch(TextField num1, TextField num2) throws NumberMismatch {
        if(!num1.getText().isBlank() && !num2.getText().isBlank() && Integer.parseInt(num1.getText()) > Integer.parseInt(num2.getText())){
            throw new NumberMismatch();
        }
    }

    default void inputType(TextField num) throws InvalidInputType {
        if(!num.getText().isBlank() && !num.getText().matches("[0-9]+"))
            throw new InvalidInputType (num.getPromptText());
    }

    default void checkListSize(int x){
        if(x<1){
            throw new NoMatchException();
        }
    }

    default void checkSelectedProduct(T inst) throws NoProductSelectedException {
        if(inst == null){
            throw new NoProductSelectedException("No product is selected!");
        }
    }

    default void checkPassword(String username, String password, Map<String, String> users){
        boolean uvjet = false;
        for(String names:users.keySet()){
            if(username.equals(names)){
                String pass = String.valueOf(password.hashCode());
                if(pass.equals(users.get(names))){
                    uvjet = true;
                }
            }
        }
        if(!uvjet){
            throw new IncorrectPasswordException("Incorrect username or password.");
        }
    }

}
