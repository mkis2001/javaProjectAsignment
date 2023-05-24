package hr.java.musicshop.entitet;

public class Synth extends Keys{
    private boolean modulationWheel;
    private boolean arpeggiator;

    public Synth(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String finishType, String color, int keyNumber, boolean arpeg, boolean modulationWheel) {
        super(id, slikaLink, proizvodac, naziv, cijena, TipInstrumenta.ELEKTRICNI, finishType, color, keyNumber);
        this.modulationWheel = modulationWheel;
        this.arpeggiator = arpeg;
    }

    public boolean isModulationWheel() {
        return modulationWheel;
    }

    public void setModulationWheel(boolean modulationWheel) {
        this.modulationWheel = modulationWheel;
    }

    public boolean isArpeggiator() {
        return arpeggiator;
    }

    public void setArpeggiator(boolean arpeggiator) {
        this.arpeggiator = arpeggiator;
    }
}
