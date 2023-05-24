package hr.java.musicshop.entitet;

public class Zicani<T extends Number, Z> extends Instrument{
    private int brojZica;
    private T scaleLenght;
    private int brPragova;
    private Wood body;
    private Wood neck;
    private Wood fretboard;
    private Z shape;

    public Zicani(Long id, String slikaLink, String proizvodac, String naziv, TipInstrumenta tipInstrumenta, int cijena,String finish, String color, int brojZica, T scaleLenght, int brPragova, String bodyWood, String neckWood, String fretboardWood, String shape) {
        super(id, slikaLink,  proizvodac, naziv, cijena, tipInstrumenta, finish, color);
        this.brojZica = brojZica;
        this.scaleLenght = scaleLenght;
        this.brPragova = brPragova;
        this.body = woodChoice(bodyWood);
        this.neck = woodChoice(neckWood);
        this.fretboard = woodChoice(fretboardWood);
    }

    public int getBrojZica() {
        return brojZica;
    }

    public void setBrojZica(int brojZica) {
        this.brojZica = brojZica;
    }

    public T getScaleLenght() {
        return scaleLenght;
    }

    public Z getShape() {
        return shape;
    }

    public void setShape(Z shape) {
        this.shape = shape;
    }

    public void setScaleLenght(T scaleLenght) {
        this.scaleLenght = scaleLenght;
    }

    public int getBrPragova() {
        return brPragova;
    }

    public void setBrPragova(int brPragova) {
        this.brPragova = brPragova;
    }

    public Wood getBody() {
        return body;
    }

    public void setBody(Wood body) {
        this.body = body;
    }

    public Wood getNeck() {
        return neck;
    }

    public void setNeck(Wood neck) {
        this.neck = neck;
    }

    public Wood getFretboard() {
        return fretboard;
    }

    public void setFretboard(Wood fretboard) {
        this.fretboard = fretboard;
    }

    private Wood woodChoice(String wood){
        switch(wood){
            case "Maple":
                return Wood.MAPLE;
            case "Mahogany":
                return Wood.MAHOGANY;
            case "Alder":
                return Wood.ALDER;
            case "Rosewood":
                return Wood.ROSEWOOD;
            case "Ebony":
                return Wood.EBONY;
            case "Roasted maple":
                return Wood.ROASTED_MAPLE;
            case "Ash":
                return Wood.ASH;
            case "Basswood":
                return Wood.BASSWOOD;
        }
        return null;
    }
}
