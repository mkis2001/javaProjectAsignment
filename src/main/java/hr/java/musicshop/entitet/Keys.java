package hr.java.musicshop.entitet;

public class Keys extends Instrument{
    private int keyNumber;

    public Keys(Long id, String slikaLink, String proizvodac, String naziv, int cijena, TipInstrumenta tipInstrumenta, String finishType, String color, int keyNumber) {
        super(id, slikaLink, proizvodac, naziv, cijena, tipInstrumenta, finishType, color);
        this.keyNumber = keyNumber;
    }

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }
}
