package hr.java.musicshop.entitet;

public enum Bridge {
    TUNEOMATIC("Tune-O-Matic", "TOM", 0, 1, "Tune-O-Matic"),
    HARDTAIL("Hardtail", "HT", 0, 2, "Hardtail"),
    TREMOLO("Tremolo", "TRM", 80, 3, "Tremolo (+80€)"),
    FLOYD("Floyd Rose", "FR", 200, 4, "Floyd Rose (+200€)");

    private String naziv;
    private String kratica;
    private int price;
    private int br;
    private String nazivSaCijenom;
    Bridge(String naziv, String kratica, int price, int br, String nazivSaCijenom) {
        this.naziv = naziv;
        this.kratica = kratica;
        this.price = price;
        this.br = br;
        this.nazivSaCijenom = nazivSaCijenom;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKratica() {
        return kratica;
    }

    public void setKratica(String kratica) {
        this.kratica = kratica;
    }

    public int getPrice() {
        return price;
    }

    public int getBr() {
        return br;
    }

    public String getNazivSaCijenom() {
        return nazivSaCijenom;
    }
}
