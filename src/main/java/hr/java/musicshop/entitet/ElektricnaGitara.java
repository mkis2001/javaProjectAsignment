package hr.java.musicshop.entitet;

import hr.java.musicshop.baza.BazaPodataka;

import java.io.Serializable;

public class ElektricnaGitara extends Zicani implements Serializable {

    public static class Builder{
        private long id;
        private String slikaLink;
        private String proizvodac;
        private String naziv;
        private int cijena;
        private String finish;
        private String color;
        private int brZica;
        private Number scaleLenght;
        private int brPragova;
        private String body;
        private String neck;
        private String fretboard;
        private String shapeString;
        private String pickups;
        private int bridge;

        public Builder(long id){
            this.id = 0;
        }

        public Builder setSlikaLink(String slikaLink){
            this.slikaLink = slikaLink;
            return this;
        }

        public Builder setProizvodac(String proizvodac){
            this.proizvodac = proizvodac;
            return this;
        }

        public Builder setNaziv(String naziv) {
            this.naziv = naziv;
            return this;
        }

        public Builder setCijena(int cijena) {
            this.cijena = cijena;
            return this;
        }

        public Builder setFinish(String finish) {
            this.finish = finish;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setBrZica(int brZica) {
            this.brZica = brZica;
            return this;
        }

        public Builder setScaleLenght(Number scaleLenght) {
            this.scaleLenght = scaleLenght;
            return this;
        }

        public Builder setBrPragova(int brPragova) {
            this.brPragova = brPragova;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setNeck(String neck) {
            this.neck = neck;
            return this;
        }

        public Builder setFretboard(String fretboard) {
            this.fretboard = fretboard;
            return this;
        }

        public Builder setShape(String shapeString){
            this.shapeString = shapeString;
            return this;
        }

        public Builder setPickups(String pickups) {
            this.pickups = pickups;
            return this;
        }

        public Builder setBridge(int bridge) {
            this.bridge = bridge;
            return this;
        }

        public ElektricnaGitara build(){
            return new ElektricnaGitara(id, slikaLink, proizvodac, naziv, cijena, finish, color, brZica, scaleLenght, brPragova, body, neck, fretboard, shapeString, pickups, bridge);
        }
    }

    private String pickupConfig;
    private Bridge bridge;
    private ElectricShape shape;
    public ElektricnaGitara(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String finish, String color, int brojZica, Number scaleLenght, int brPragova, String bodyWood, String neckWood, String fretboardWood, String shape, String pickupConfig, int bridgeBr) {
        super(id, slikaLink, proizvodac, naziv, TipInstrumenta.ELEKTRICNI, cijena, finish, color, brojZica, scaleLenght, brPragova, bodyWood, neckWood, fretboardWood, shape);
        this.pickupConfig = pickupConfig;
        switch (bridgeBr) {
            case 1 -> this.bridge = Bridge.TUNEOMATIC;
            case 2 -> this.bridge = Bridge.HARDTAIL;
            case 3 -> this.bridge = Bridge.TREMOLO;
            case 4 -> this.bridge = Bridge.FLOYD;
        }
        switch (shape){
            case "Stratocaster" -> this.shape = ElectricShape.STRAT;
            case "Telecaster" -> this.shape = ElectricShape.TELE;
            case "Single cut" -> this.shape = ElectricShape.LP;
            case "Offset" -> this.shape = ElectricShape.OFFSET;
            case "Alternative" -> this.shape = ElectricShape.ALTERNATIVE;
        }
    }
    public String getPickupConfig() {
        return pickupConfig;
    }

    public Bridge getBridge() {
        return bridge;
    }

    @Override
    public ElectricShape getShape() {
        return shape;
    }
}
