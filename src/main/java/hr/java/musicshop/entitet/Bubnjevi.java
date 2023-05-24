package hr.java.musicshop.entitet;

import java.util.HashMap;
import java.util.Map;

public class Bubnjevi<T> extends Instrument{

    private Map<DrumPart, Integer> mapaDijelova;
    private T snareSize;
    private T kickSize;

    public Bubnjevi(Long id, String slikaLink, String proizvodac, String naziv, int cijena, TipInstrumenta tipInstrumenta, String finishType, String color, String kod, T snareSize, T kickSize) {
        super(id, slikaLink, proizvodac, naziv, cijena, tipInstrumenta, finishType, color);
        this.mapaDijelova = popunaMapeDijelova(kod);
        this.snareSize = snareSize;
        this.kickSize = kickSize;
    }

    private Map<DrumPart, Integer> popunaMapeDijelova(String kod){
        Map<DrumPart, Integer> pomocni = new HashMap<>();
        pomocni.put(DrumPart.SNARE, Integer.parseInt(String.valueOf(kod.charAt(0))));
        pomocni.put(DrumPart.KICK, Integer.parseInt(String.valueOf(kod.charAt(1))));
        pomocni.put(DrumPart.TOM, Integer.parseInt(String.valueOf(kod.charAt(2))));
        pomocni.put(DrumPart.FLOORTOM, Integer.parseInt(String.valueOf(kod.charAt(3))));
        pomocni.put(DrumPart.HIHAT, Integer.parseInt(String.valueOf(kod.charAt(4))));
        pomocni.put(DrumPart.RIDE, Integer.parseInt(String.valueOf(kod.charAt(5))));
        pomocni.put(DrumPart.CRASH, Integer.parseInt(String.valueOf(kod.charAt(6))));
        return pomocni;
    }

    public Map<DrumPart, Integer> getMapaDijelova() {
        return mapaDijelova;
    }

    public int getDrumPart(DrumPart drumPart){
        return mapaDijelova.get(drumPart);
    }

    public void setMapaDijelova(Map<DrumPart, Integer> mapaDijelova) {
        this.mapaDijelova = mapaDijelova;
    }

    public T getSnareSize() {
        return snareSize;
    }

    public void setSnareSize(T snareSize) {
        this.snareSize = snareSize;
    }

    public T getKickSize() {
        return kickSize;
    }

    public void setKickSize(T kickSize) {
        this.kickSize = kickSize;
    }
}
