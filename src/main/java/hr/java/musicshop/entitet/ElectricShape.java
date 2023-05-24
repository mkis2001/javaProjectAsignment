package hr.java.musicshop.entitet;

public enum ElectricShape {
    STRAT("Stratocaster"),
    TELE("Telecaster"),
    LP("Single cut"),
    OFFSET("Offset"),
    ALTERNATIVE("Alternative");

    private String naziv;

    ElectricShape(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}
