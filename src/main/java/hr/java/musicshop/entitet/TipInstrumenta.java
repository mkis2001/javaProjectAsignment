package hr.java.musicshop.entitet;

public enum TipInstrumenta {
    AKUSTICNI("Acoustic", 0),
    ELEKTRICNI("Electric", 1);
    String naziv;

    TipInstrumenta(String naziv, int brTipa) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}
