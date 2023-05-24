package hr.java.musicshop.entitet;

public abstract class Instrument {
    private Long id;
    private String slikaLink;
    private String proizvodac;
    private String naziv;
    private int cijena;
    private TipInstrumenta tipInstrumenta;
    private FinishType finish;
    private String color;

    public Instrument(Long id, String slikaLink, String proizvodac, String naziv, int cijena, TipInstrumenta tipInstrumenta, String finishType, String color) {
        this.id = id;
        this.slikaLink = slikaLink;
        this.proizvodac = proizvodac;
        this.naziv = naziv;
        this.cijena = cijena;
        this.tipInstrumenta = tipInstrumenta;
        switch(finishType) {
            case "Natural" -> this.finish = FinishType.NATURAL;
            case "Solid" -> this.finish = FinishType.SOLID;
            case "Translucent" -> this.finish = FinishType.TRANSLUCENT;
            case "Sparkle" -> this.finish = FinishType.SPARKLE;
        }
        this.color = color;
    }

    public Instrument(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlikaLink() {
        return slikaLink;
    }

    public void setSlikaLink(String slikaLink) {
        this.slikaLink = slikaLink;
    }

    public String getProizvodac() {
        return proizvodac;
    }

    public void setProizvodac(String proizvodac) {
        this.proizvodac = proizvodac;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getCijena() {
        return cijena;
    }

    public void setCijena(int cijena) {
        this.cijena = cijena;
    }

    public TipInstrumenta getTipInstrumenta() {
        return tipInstrumenta;
    }

    public void setTipInstrumenta(TipInstrumenta tipInstrumenta) {
        this.tipInstrumenta = tipInstrumenta;
    }

    public FinishType getFinish() {
        return finish;
    }

    public void setFinish(FinishType finish) {
        this.finish = finish;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
