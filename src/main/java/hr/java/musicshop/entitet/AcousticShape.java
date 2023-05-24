package hr.java.musicshop.entitet;

public enum AcousticShape {
    DREADNOUGHT("Dreadnought", "DRD"),
    JUMBO("Jumbo", "JMB"),
    FOLK("Folk", "FLK");

    private final String naziv;
    private final String kratica;

    AcousticShape(String naziv, String kratica) {
        this.naziv = naziv;
        this.kratica = kratica;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getKratica(){return kratica;}

}
