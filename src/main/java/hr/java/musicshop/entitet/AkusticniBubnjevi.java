package hr.java.musicshop.entitet;

public class AkusticniBubnjevi extends Bubnjevi{

    public AkusticniBubnjevi(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String finishType, String color, String kod, String snare, String kick) {
        super(id, slikaLink, proizvodac, naziv, cijena, TipInstrumenta.AKUSTICNI, finishType, color, kod, snare, kick);
    }
}
