package hr.java.musicshop.baza;

import hr.java.musicshop.entitet.*;

import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class BazaPodataka {

    private static Connection connectToDatabase() throws Exception {
        Properties spajanje = new Properties();
        spajanje.load(new FileInputStream("dat/dataBase.properties"));

        Connection con = DriverManager.getConnection(
                spajanje.getProperty("dataBaseURL"),
                spajanje.getProperty("username"),
                spajanje.getProperty("password")
        );

        return con;
    }

    //Elektricne gitare
    public synchronized static List<ElektricnaGitara> getElGitare(){
        try {
            Connection con = connectToDatabase();
            List<ElektricnaGitara> elGitare = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EL_GITARE");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                int brZica = rs.getInt("br_zica");
                double scale = rs.getDouble("scale_lenght");
                int brPragova = rs.getInt("br_pragova");
                String body = rs.getString("body");
                String neck = rs.getString("neck");
                String fretboard = rs.getString("fretboard");
                String shape = rs.getString("shape");
                String pickups = rs.getString("pickups");
                int bridge = rs.getInt("bridge");
                if(scale - (int)scale != 0){
                    elGitare.add(new ElektricnaGitara(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scale, brPragova, body, neck, fretboard, shape, pickups, bridge));
                }else {
                    int scaleLenght = (int) scale;
                    elGitare.add(new ElektricnaGitara(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scaleLenght, brPragova, body, neck, fretboard, shape, pickups, bridge));
                }
            }
            con.close();
            return elGitare;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addCustomGuitar(ElektricnaGitara gitara) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement stmt = con.prepareStatement("INSERT INTO EL_GITARE(slika, proizvodac, naziv, cijena, finish, color, br_zica, scale_lenght, br_pragova, body, neck, fretboard, shape, pickups, bridge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, gitara.getSlikaLink());
        stmt.setString(2, gitara.getProizvodac());
        stmt.setString(3, gitara.getNaziv());
        stmt.setInt(4, gitara.getCijena());
        stmt.setString(5, gitara.getFinish().getNaziv());
        stmt.setString(6, gitara.getColor());
        stmt.setInt(7, gitara.getBrojZica());
        stmt.setDouble(8, (Double) gitara.getScaleLenght());
        stmt.setInt(9, gitara.getBrPragova());
        stmt.setString(10, gitara.getBody().getNaziv());
        stmt.setString(11, gitara.getNeck().getNaziv());
        stmt.setString(12, gitara.getFretboard().getNaziv());
        stmt.setString(13, gitara.getShape().getNaziv());
        stmt.setString(14, gitara.getPickupConfig());
        stmt.setInt(15, gitara.getBridge().getBr());

        stmt.executeUpdate();

        con.close();
    }

    public synchronized static void changeGuitar(Long id, ElektricnaGitara gitara, String changes) {
        try{
            Connection con = connectToDatabase();

            PreparedStatement updateName = con.prepareStatement("UPDATE EL_GITARE SET NAZIV = ? WHERE ID = ?");
            updateName.setString(1, gitara.getNaziv());
            updateName.setString(2, String.valueOf(id));
            updateName.executeUpdate();

            if(changes.contains("SHAPE")){
                PreparedStatement updateShape = con.prepareStatement("UPDATE EL_GITARE SET SHAPE = ? WHERE ID = ?");
                System.out.println("change");
                updateShape.setString(1, gitara.getShape().getNaziv());
                updateShape.setString(2, String.valueOf(id));
                updateShape.executeUpdate();
            }
            if(changes.contains("BODY")){
                PreparedStatement updateBody = con.prepareStatement("UPDATE EL_GITARE SET BODY = ? WHERE ID = ?");
                updateBody.setString(1, gitara.getBody().getNaziv());
                updateBody.setString(2, String.valueOf(id));
                updateBody.executeUpdate();
            }
            if(changes.contains("NECK")){
                PreparedStatement updateNeck = con.prepareStatement("UPDATE EL_GITARE SET NECK = ? WHERE ID = ?");
                updateNeck.setString(1, gitara.getNeck().getNaziv());
                updateNeck.setString(2, String.valueOf(id));
                updateNeck.executeUpdate();
            }
            if(changes.contains("FRETBOARD")){
                PreparedStatement updateFretboard = con.prepareStatement("UPDATE EL_GITARE SET FRETBOARD = ? WHERE ID = ?");
                updateFretboard.setString(1, gitara.getFretboard().getNaziv());
                updateFretboard.setString(2, String.valueOf(id));
                updateFretboard.executeUpdate();
            }
            if(changes.contains("STRINGS")){
                PreparedStatement updateStrings = con.prepareStatement("UPDATE EL_GITARE SET BR_ZICA = ? WHERE ID = ?");
                updateStrings.setInt(1, gitara.getBrojZica());
                updateStrings.setString(2, String.valueOf(id));
                updateStrings.executeUpdate();
            }
            if(changes.contains("SCALE")){
                PreparedStatement updateScale = con.prepareStatement("UPDATE EL_GITARE SET SCALE_LENGHT = ? WHERE ID = ?");
                updateScale.setDouble(1, (double) gitara.getScaleLenght());
                updateScale.setString(2, String.valueOf(id));
                updateScale.executeUpdate();
            }
            if(changes.contains("PICKUPS")){
                PreparedStatement updatePickups = con.prepareStatement("UPDATE EL_GITARE SET PICKUPS = ? WHERE ID = ?");
                updatePickups.setString(1, gitara.getPickupConfig());
                updatePickups.setString(2, String.valueOf(id));
                updatePickups.executeUpdate();
            }
            if(changes.contains("FRETS")){
                PreparedStatement updateFrets = con.prepareStatement("UPDATE EL_GITARE SET BR_PRAGOVA = ? WHERE ID = ?");
                updateFrets.setInt(1, gitara.getBrPragova());
                updateFrets.setString(2, String.valueOf(id));
                updateFrets.executeUpdate();
            }
            if(changes.contains("BRIDGE")){
                PreparedStatement updateBridge = con.prepareStatement("UPDATE EL_GITARE SET BRIDGE = ? WHERE ID = ?");
                updateBridge.setInt(1, gitara.getBridge().getBr());
                updateBridge.setString(2, String.valueOf(id));
                updateBridge.executeUpdate();
            }
            if(changes.contains("FINISH")){
                PreparedStatement updateFinish = con.prepareStatement("UPDATE EL_GITARE SET FINISH = ? WHERE ID = ?");
                updateFinish.setString(1, gitara.getFinish().getNaziv());
                updateFinish.setString(2, String.valueOf(id));
                updateFinish.executeUpdate();
            }
            if(changes.contains("COLOR")){
                PreparedStatement updateColor = con.prepareStatement("UPDATE EL_GITARE SET COLOR = ? WHERE ID = ?");
                updateColor.setString(1, gitara.getColor());
                updateColor.setString(2, String.valueOf(id));
                updateColor.executeUpdate();
            }
            con.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Akusticne gitare
    public synchronized static List<AkusticnaGitara> getAkGitare(){
        try {
            Connection con = connectToDatabase();
            List<AkusticnaGitara> akGitare = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AC_GITARE");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                int brZica = rs.getInt("br_zica");
                double scale = rs.getDouble("scale_lenght");
                int brPragova = rs.getInt("br_pragova");
                String body = rs.getString("body");
                String neck = rs.getString("neck");
                String fretboard = rs.getString("fretboard");
                String shape = rs.getString("shape");
                boolean lineOut = rs.getBoolean("line_out");
                if (scale - (int) scale != 0) {
                    akGitare.add(new AkusticnaGitara(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scale, brPragova, body, neck, fretboard, shape, lineOut));
                }else{
                    int scaleLenght = (int) scale;
                    akGitare.add(new AkusticnaGitara(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scaleLenght, brPragova, body, neck, fretboard, shape, lineOut));
                }
            }
            con.close();
            return akGitare;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addCustomAcoustic(AkusticnaGitara gitara) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement stmt = con.prepareStatement("INSERT INTO AC_GITARE(slika, proizvodac, naziv, cijena, finish, color, br_zica, scale_lenght, br_pragova, body, neck, fretboard, shape, line_out) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, gitara.getSlikaLink());
        stmt.setString(2, gitara.getProizvodac());
        stmt.setString(3, gitara.getNaziv());
        stmt.setInt(4, gitara.getCijena());
        stmt.setString(5, gitara.getFinish().getNaziv());
        stmt.setString(6, gitara.getColor());
        stmt.setInt(7, gitara.getBrojZica());
        stmt.setDouble(8, (Double) gitara.getScaleLenght());
        stmt.setInt(9, gitara.getBrPragova());
        stmt.setString(10, gitara.getBody().getNaziv());
        stmt.setString(11, gitara.getNeck().getNaziv());
        stmt.setString(12, gitara.getFretboard().getNaziv());
        stmt.setString(13, gitara.getShape().getNaziv());
        stmt.setBoolean(14, gitara.getLineOut());

        stmt.executeUpdate();

        con.close();
    }

    public synchronized static void changeAcousticGuitar(Long id, AkusticnaGitara gitara, String changes) {
        try{
            Connection con = connectToDatabase();

            PreparedStatement updateName = con.prepareStatement("UPDATE AC_GITARE SET NAZIV = ? WHERE ID = ?");
            updateName.setString(1, gitara.getNaziv());
            updateName.setString(2, String.valueOf(id));
            updateName.executeUpdate();

            if(changes.contains("SHAPE")){
                PreparedStatement updateShape = con.prepareStatement("UPDATE AC_GITARE SET SHAPE = ? WHERE ID = ?");
                System.out.println("change");
                updateShape.setString(1, gitara.getShape().getNaziv());
                updateShape.setString(2, String.valueOf(id));
                updateShape.executeUpdate();
            }
            if(changes.contains("BODY")){
                PreparedStatement updateBody = con.prepareStatement("UPDATE AC_GITARE SET BODY = ? WHERE ID = ?");
                updateBody.setString(1, gitara.getBody().getNaziv());
                updateBody.setString(2, String.valueOf(id));
                updateBody.executeUpdate();
            }
            if(changes.contains("NECK")){
                PreparedStatement updateNeck = con.prepareStatement("UPDATE AC_GITARE SET NECK = ? WHERE ID = ?");
                updateNeck.setString(1, gitara.getNeck().getNaziv());
                updateNeck.setString(2, String.valueOf(id));
                updateNeck.executeUpdate();
            }
            if(changes.contains("FRETBOARD")){
                PreparedStatement updateFretboard = con.prepareStatement("UPDATE AC_GITARE SET FRETBOARD = ? WHERE ID = ?");
                updateFretboard.setString(1, gitara.getFretboard().getNaziv());
                updateFretboard.setString(2, String.valueOf(id));
                updateFretboard.executeUpdate();
            }
            if(changes.contains("SCALE")){
                PreparedStatement updateScale = con.prepareStatement("UPDATE AC_GITARE SET SCALE_LENGHT = ? WHERE ID = ?");
                updateScale.setDouble(1, (double) gitara.getScaleLenght());
                updateScale.setString(2, String.valueOf(id));
                updateScale.executeUpdate();
            }
            if(changes.contains("FRETS")){
                PreparedStatement updateFrets = con.prepareStatement("UPDATE AC_GITARE SET BR_PRAGOVA = ? WHERE ID = ?");
                updateFrets.setInt(1, gitara.getBrPragova());
                updateFrets.setString(2, String.valueOf(id));
                updateFrets.executeUpdate();
            }
            if(changes.contains("FINISH")){
                PreparedStatement updateFinish = con.prepareStatement("UPDATE AC_GITARE SET FINISH = ? WHERE ID = ?");
                updateFinish.setString(1, gitara.getFinish().getNaziv());
                updateFinish.setString(2, String.valueOf(id));
                updateFinish.executeUpdate();
            }
            if(changes.contains("COLOR")){
                PreparedStatement updateColor = con.prepareStatement("UPDATE AC_GITARE SET COLOR = ? WHERE ID = ?");
                updateColor.setString(1, gitara.getColor());
                updateColor.setString(2, String.valueOf(id));
                updateColor.executeUpdate();
            }
            if(changes.contains("LINE OUT")){
                PreparedStatement updateColor = con.prepareStatement("UPDATE AC_GITARE SET LINE_OUT = ? WHERE ID = ?");
                updateColor.setBoolean(1, gitara.getLineOut());
                updateColor.setString(2, String.valueOf(id));
                updateColor.executeUpdate();
            }

            con.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Bass
    public synchronized static List<Bass> getBassGitare(){
        try {
            Connection con = connectToDatabase();
            List<Bass> bass = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BASS");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                int brZica = rs.getInt("br_zica");
                double scale = rs.getDouble("scale_lenght");
                int brPragova = rs.getInt("br_pragova");
                String body = rs.getString("body");
                String neck = rs.getString("neck");
                String fretboard = rs.getString("fretboard");
                String shape = rs.getString("shape");
                String pickups = rs.getString("pickups");
                boolean preamp = rs.getBoolean("preamp");
                if (scale - (int) scale != 0) {
                    bass.add(new Bass(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scale, brPragova, body, neck, fretboard, shape, pickups, preamp));
                }else{
                    int scaleLenght = (int)scale;
                    bass.add(new Bass(id, slika, proizvodac, naziv, cijena, finish, color, brZica, scaleLenght, brPragova, body, neck, fretboard, shape, pickups, preamp));
                }
            }
            con.close();
            return bass;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addCustomBass(Bass bass) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement stmt = con.prepareStatement("INSERT INTO BASS(slika, proizvodac, naziv, cijena, finish, color, br_zica, scale_lenght, br_pragova, body, neck, fretboard, shape, pickups, preamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, bass.getSlikaLink());
        stmt.setString(2, bass.getProizvodac());
        stmt.setString(3, bass.getNaziv());
        stmt.setInt(4, bass.getCijena());
        stmt.setString(5, bass.getFinish().getNaziv());
        stmt.setString(6, bass.getColor());
        stmt.setInt(7, bass.getBrojZica());
        stmt.setDouble(8, (Double) bass.getScaleLenght());
        stmt.setInt(9, bass.getBrPragova());
        stmt.setString(10, bass.getBody().getNaziv());
        stmt.setString(11, bass.getNeck().getNaziv());
        stmt.setString(12, bass.getFretboard().getNaziv());
        stmt.setString(13, bass.getShape().getNaziv());
        stmt.setString(14, bass.getPickupConfig());
        stmt.setBoolean(15, bass.getPreamp());

        stmt.executeUpdate();

        con.close();
    }

    public synchronized static void changeBass(Long id, Bass bass, String changes){
        try{
            Connection con = connectToDatabase();

            PreparedStatement updateName = con.prepareStatement("UPDATE BASS SET NAZIV = ? WHERE ID = ?");
            updateName.setString(1, bass.getNaziv());
            updateName.setString(2, String.valueOf(id));
            updateName.executeUpdate();

            if(changes.contains("SHAPE")){
                PreparedStatement updateShape = con.prepareStatement("UPDATE BASS SET SHAPE = ? WHERE ID = ?");
                System.out.println("change");
                updateShape.setString(1, bass.getShape().getNaziv());
                updateShape.setString(2, String.valueOf(id));
                updateShape.executeUpdate();
            }
            if(changes.contains("BODY")){
                PreparedStatement updateBody = con.prepareStatement("UPDATE BASS SET BODY = ? WHERE ID = ?");
                updateBody.setString(1, bass.getBody().getNaziv());
                updateBody.setString(2, String.valueOf(id));
                updateBody.executeUpdate();
            }
            if(changes.contains("NECK")){
                PreparedStatement updateNeck = con.prepareStatement("UPDATE BASS SET NECK = ? WHERE ID = ?");
                updateNeck.setString(1, bass.getNeck().getNaziv());
                updateNeck.setString(2, String.valueOf(id));
                updateNeck.executeUpdate();
            }
            if(changes.contains("FRETBOARD")){
                PreparedStatement updateFretboard = con.prepareStatement("UPDATE BASS SET FRETBOARD = ? WHERE ID = ?");
                updateFretboard.setString(1, bass.getFretboard().getNaziv());
                updateFretboard.setString(2, String.valueOf(id));
                updateFretboard.executeUpdate();
            }
            if(changes.contains("STRINGS")){
                PreparedStatement updateStrings = con.prepareStatement("UPDATE BASS SET BR_ZICA = ? WHERE ID = ?");
                updateStrings.setInt(1, bass.getBrojZica());
                updateStrings.setString(2, String.valueOf(id));
                updateStrings.executeUpdate();
            }
            if(changes.contains("SCALE")){
                PreparedStatement updateScale = con.prepareStatement("UPDATE BASS SET SCALE_LENGHT = ? WHERE ID = ?");
                updateScale.setDouble(1, (double) bass.getScaleLenght());
                updateScale.setString(2, String.valueOf(id));
                updateScale.executeUpdate();
            }
            if(changes.contains("PICKUPS")){
                PreparedStatement updatePickups = con.prepareStatement("UPDATE BASS SET PICKUPS = ? WHERE ID = ?");
                updatePickups.setString(1, bass.getPickupConfig());
                updatePickups.setString(2, String.valueOf(id));
                updatePickups.executeUpdate();
            }
            if(changes.contains("FRETS")){
                PreparedStatement updateFrets = con.prepareStatement("UPDATE BASS SET BR_PRAGOVA = ? WHERE ID = ?");
                updateFrets.setInt(1, bass.getBrPragova());
                updateFrets.setString(2, String.valueOf(id));
                updateFrets.executeUpdate();
            }
            if(changes.contains("PREAMP")){
                PreparedStatement updateColor = con.prepareStatement("UPDATE BASS SET PREAMP = ? WHERE ID = ?");
                updateColor.setBoolean(1, bass.getPreamp());
                updateColor.setString(2, String.valueOf(id));
                updateColor.executeUpdate();
            }
            if(changes.contains("FINISH")){
                PreparedStatement updateFinish = con.prepareStatement("UPDATE BASS SET FINISH = ? WHERE ID = ?");
                updateFinish.setString(1, bass.getFinish().getNaziv());
                updateFinish.setString(2, String.valueOf(id));
                updateFinish.executeUpdate();
            }
            if(changes.contains("COLOR")){
                PreparedStatement updateColor = con.prepareStatement("UPDATE BASS SET COLOR = ? WHERE ID = ?");
                updateColor.setString(1, bass.getColor());
                updateColor.setString(2, String.valueOf(id));
                updateColor.executeUpdate();
            }
            con.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Akusticni bubnjevi
    public synchronized static List<AkusticniBubnjevi> getAkBubnjeve(){
        try {
            Connection con = connectToDatabase();
            List<AkusticniBubnjevi> akBubnjevi = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AC_DRUMS");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                String dijelovi = rs.getString("dijelovi");
                String snare = rs.getString("snare");
                String kick = rs.getString("kick");
                akBubnjevi.add(new AkusticniBubnjevi(id, slika, proizvodac, naziv, cijena, finish, color, dijelovi, snare, kick));
            }
            con.close();
            return akBubnjevi;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Elektricni bubnjevi
    public synchronized static List<ElektricniBubnjevi> getElDrums(){
        try {
            Connection con = connectToDatabase();
            List<ElektricniBubnjevi> elBubnjevi = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EL_DRUMS");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String color = rs.getString("color");
                String dijelovi = rs.getString("dijelovi");
                int snare = rs.getInt("snare");
                int kick = rs.getInt("kick");
                int kits = rs.getInt("kits");
                elBubnjevi.add(new ElektricniBubnjevi(id, slika, proizvodac, naziv, cijena, color, dijelovi, snare, kick, kits));
            }
            con.close();
            return elBubnjevi;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Pianos
    public synchronized static List<Piano> getPianos(){
        try {
            Connection con = connectToDatabase();
            List<Piano> pianos = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PIANOS");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                int weight = rs.getInt("weight");
                int height = rs.getInt("height");
                pianos.add(new Piano(id, slika, proizvodac, naziv, cijena, finish, color, weight, height));
            }
            con.close();
            return pianos;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Synthesizers
    public synchronized static List<Synth> getSynths() {
        try {
            Connection con = connectToDatabase();
            List<Synth> synths = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SYNTHESIZERS");

            while (rs.next()) {
                Long id = rs.getLong("id");
                String slika = rs.getString("slika");
                String proizvodac = rs.getString("proizvodac");
                String naziv = rs.getString("naziv");
                int cijena = rs.getInt("cijena");
                String finish = rs.getString("finish");
                String color = rs.getString("color");
                int keys = rs.getInt("keys");
                boolean arpeg = rs.getBoolean("arpeg");
                boolean mod_wheel = rs.getBoolean("mod_wheel");
                synths.add(new Synth(id, slika, proizvodac, naziv, cijena, finish, color, keys, arpeg, mod_wheel));
            }
            con.close();
            return synths;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void deleteInstrument(String type, Long id, String naziv, String cijena) throws Exception {
        Connection con = connectToDatabase();

        String inst = null;
        String tip = null;

        switch (type){
            case "elGuitars" -> {
                inst = "EL_GITARE";
                tip = "Electric guitar";
            }
            case "acGuitars" ->{
                inst = "AC_GITARE";
                tip = "Acoustic guitar";
            }
            case "bass" -> {
                inst = "BASS";
                tip = "Bass guitar";
            }
            case "acDrums" -> {
                inst = "AC_DRUMS";
                tip = "Acoustic drum";
            }
            case "elDrums" -> {
                inst = "EL_DRUMS";
                tip = "Electric drum";
            }
            case "piano" -> {
                inst = "PIANOS";
                tip = "Piano";
            }
            case "synth" -> {
                inst = "SYNTHESIZERS";
                tip = "Synthesizer";
            }
        }

        PreparedStatement delete = con.prepareStatement("DELETE FROM " + inst + " WHERE ID = ?");
        delete.setString(1, String.valueOf(id));
        delete.executeUpdate();
        con.close();

        addSoldInstrument(naziv, cijena, tip);
    }

    private synchronized static void addSoldInstrument(String naziv, String cijena, String tip){
        try{
            Connection con = connectToDatabase();

            PreparedStatement stmt = con.prepareStatement("INSERT INTO SOLD(naziv, cijena, tip) VALUES (?, ?, ?);");

            stmt.setString(1, naziv);
            stmt.setString(2, cijena);
            stmt.setString(3, tip);

            stmt.executeUpdate();

            con.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static Set<String> getSoldInstruments(){
        try{
            Connection con = connectToDatabase();

            Set<String> instrumenti = new HashSet<>();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM SOLD");


            while(rs.next()){
                String s = rs.getString("naziv");
                s = s + ", " + rs.getString("cijena");
                s = s + ", " + rs.getString("tip");
                instrumenti.add(s);
            }
            con.close();
            return instrumenti;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
