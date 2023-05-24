package hr.java.musicshop.entitet;

public enum FinishType {
    NATURAL("Natural", 0, "Natural"),
    SOLID("Solid", 0, "Solid"),
    TRANSLUCENT("Translucent", 100, "Translucent (+100€)"),
    SPARKLE("Sparkle", 200, "Sparkle (+200€)");

    private String naziv;
    private int price;
    private String nazivSaCijenom;
    FinishType(String naziv, int price, String nazivSaCijenom){
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

    public String getNazivSaCijenom(){
        return nazivSaCijenom;
    }
}
