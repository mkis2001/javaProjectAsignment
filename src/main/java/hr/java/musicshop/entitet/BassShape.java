package hr.java.musicshop.entitet;

public enum BassShape {
    JAZZ("Jazz", "J"),
    P("P shape", "P"),
    ALTERNATIVE("Alternative", "Alt");

    private String naziv;
    private String kratica;

    BassShape(String naziv, String kratica) {
        this.naziv = naziv;
        this.kratica = kratica;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getKratica() {
        return kratica;
    }
}
