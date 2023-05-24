package hr.java.musicshop.entitet;

public enum Wood {

    MAPLE("Maple", 0, "Maple"),
    ALDER("Alder", 0, "Alder"),
    MAHOGANY("Mahogany", 100, "Mahogany (+100€)"),
    ROSEWOOD("Rosewood", 30, "Rosewood (+30€)"),
    EBONY("Ebony", 100, "Ebony (+100€)"),
    ROASTED_MAPLE("Roasted maple", 100, "Roasted maple (+100€)"),
    ASH("Ash", 100, "Ash (+100€)"),
    BASSWOOD("Basswood", 0, "Basswood");

    private String naziv;
    private int price;
    private String nazivSaCijenom;
    Wood(String naziv, Integer price, String nazivSaCijenom){
        this.naziv = naziv;
        this.price = price;
        this.nazivSaCijenom = nazivSaCijenom;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getPrice() {
        return price;
    }

    public String getNazivSaCijenom() {
        return nazivSaCijenom;
    }
}
