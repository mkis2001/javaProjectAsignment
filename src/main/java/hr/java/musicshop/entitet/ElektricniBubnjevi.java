package hr.java.musicshop.entitet;

public class ElektricniBubnjevi extends Bubnjevi{
    private int kits;

    public ElektricniBubnjevi(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String color, String kod, int snareSize, int kickSize, int kits) {
        super(id, slikaLink, proizvodac, naziv, cijena, TipInstrumenta.ELEKTRICNI, "Solid", color, kod, snareSize, kickSize);
        this.kits = kits;
    }

    public int getKits() {
        return kits;
    }

    public void setKits(int kits) {
        this.kits = kits;
    }
}
