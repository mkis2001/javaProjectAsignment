package hr.java.musicshop.sucelja;

import hr.java.musicshop.baza.BazaPodataka;
import hr.java.musicshop.controller.HelloApplication;
import hr.java.musicshop.entitet.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static hr.java.musicshop.controller.HelloApplication.logger;

public interface LoadFile<T extends Zicani>{
    default List<String> ucitaj(String path){
        List<String> podaci;

        try (BufferedReader bufReader = new BufferedReader(new FileReader(path))) {
            podaci = bufReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return podaci;
    }

    default Map<String, String> getUsers(){
        Map<String, String> users = new HashMap<>();
        List<String> bufUsers = ucitaj("dat/password");
        for(int i=0;i<bufUsers.size();i+=2){
            users.put(bufUsers.get(i), bufUsers.get(i+1));
        }
        return users;
    }

    default void addUser(String username, String password){
        try {
            FileWriter writer = new FileWriter("dat/password", true);
            BufferedWriter out = new BufferedWriter(writer);
            out.write( username + "\n" + password + "\n");
            out.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssInfoDialog.css")).toExternalForm());
            dialogPane.getStyleClass().add("informationDialog");
            alert.setTitle("User added");
            alert.setHeaderText(null);
            alert.setContentText("Account '" + username + "' succesfully created.");

            alert.showAndWait();

            dodajDogadaj(new Dogadaj("Account '" + username + "' created."));
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    default void currentUser(String username){
        try{
            FileWriter writer = new FileWriter("dat/currentUser", false);
            BufferedWriter out = new BufferedWriter(writer);

            out.write(username);
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    default String getCurrentUser(){
        List<String> user = ucitaj("dat/currentUser");
        return user.get(0);
    }

    default void deleteUser(String username){
        Map<String, String> users = getUsers();
        List<String> podaci = new ArrayList<>();
        boolean userPostoji = false;
        for(String names:users.keySet()){
            if(!username.equals(names)){
                podaci.add(names);
                podaci.add(users.get(names));
            }else{
                userPostoji = true;
            }
        }
        if(!userPostoji){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSS1.css")).toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Warning");
            alert.setHeaderText("User '" + username + "' does not exist!");
            alert.setContentText(null);

            alert.showAndWait();
            logger.error("User doesn't exist.");
        }else {
            try {
                FileWriter writer = new FileWriter("dat/password", false);
                BufferedWriter out = new BufferedWriter(writer);
                if(podaci.size()!=0) {
                    out.write(podaci.get(0) +"\n");
                    for (int i = 1; i < podaci.size(); i++) {
                        out.write(podaci.get(i) + "\n");
                    }
                }
                out.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("cssInfoDialog.css")).toExternalForm());
                dialogPane.getStyleClass().add("informationDialog");
                alert.setTitle("Account deleted");
                alert.setHeaderText(null);
                alert.setContentText("Account '" + username + "' succesfully deleted.");

                alert.showAndWait();

                dodajDogadaj(new Dogadaj("User '" + username + "' deleted."));
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    default void startDogadaj() throws IOException, ClassNotFoundException {
        Dogadaj d = new Dogadaj("Program started");
        d.setUser("none");
        List<Dogadaj> dogadaji = getDogadaji();
        dogadaji.add(d);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/dogadaji.dat", false));
        out.writeObject(dogadaji);
        out.close();
    }

    default List<Dogadaj> getDogadaji() throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = new FileInputStream("dat/dogadaji.dat");
            ObjectInputStream in = new ObjectInputStream(fis);
            List<Dogadaj> dogadajiLista = new ArrayList<>();
            if(fis.available() != 0) {
                dogadajiLista = (List<Dogadaj>) in.readObject();
                return dogadajiLista;
            }else{
                dogadajiLista.add(new Dogadaj("Prvi dogadaj"));
                return dogadajiLista;
            }
        }catch (EOFException e){
            List<Dogadaj> dogadajiLista = new ArrayList<>();
            dogadajiLista.add(new Dogadaj("Prvi dogadaj"));
            return dogadajiLista;
        }
    }

    default void dodajDogadaj(Dogadaj d) throws IOException, ClassNotFoundException {
        List<Dogadaj> dogadaji = getDogadaji();
        if(d.getNaziv().equals("Incorrect username or password."))
            d.setUser("none");
        dogadaji.add(d);
        ObjectOutputStream dogadajOut = new ObjectOutputStream(new FileOutputStream("dat/dogadaji.dat"));
        dogadajOut.writeObject(dogadaji);
        dogadajOut.close();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------

    default void addGuitar(Long id, String className) {
        try(BufferedWriter out = new BufferedWriter(new FileWriter("dat/selectedGuitar"))){
            out.write(id + "\n" + className);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    default Zicani getSelectedGuitar(){
        try{
            List<String> in = ucitaj("dat/selectedGuitar");
            Long id = Long.valueOf(in.get(0));
            String type = in.get(1);
            if(type.equals("ElektricnaGitara")) {
                for (ElektricnaGitara g : BazaPodataka.getElGitare()) {
                    if (g.getId().equals(id)) {
                        return g;
                    }
                }
            }else if(type.equals("AkusticnaGitara")){
                for (AkusticnaGitara g : BazaPodataka.getAkGitare()) {
                    if (g.getId().equals(id)) {
                        return g;
                    }
                }
            }else{
                for (Bass g : BazaPodataka.getBassGitare()) {
                    if (g.getId().equals(id)) {
                        return g;
                    }
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
