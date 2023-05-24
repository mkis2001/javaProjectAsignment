package hr.java.musicshop.entitet;

public class AkusticnaGitara extends Zicani{

    private AcousticShape shape;
    private Boolean lineOut;

    public AkusticnaGitara(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String finish, String color, int brojZica, Number scaleLenght, int brPragova, String bodyWood, String neckWood, String fretboardWood, String shape, Boolean lineOut) {
        super(id, slikaLink, proizvodac, naziv, TipInstrumenta.AKUSTICNI, cijena, finish, color, brojZica, scaleLenght, brPragova, bodyWood, neckWood, fretboardWood, shape);
        switch(shape){
            case "Dreadnought" -> this.shape = AcousticShape.DREADNOUGHT;
            case "Jumbo" -> this.shape = AcousticShape.JUMBO;
            case "Folk" -> this.shape = AcousticShape.FOLK;
        }
        this.lineOut = lineOut;
    }

    public AcousticShape getShape() {
        return shape;
    }

    public void setShape(AcousticShape shape) {
        this.shape = shape;
    }

    public Boolean getLineOut() {
        return lineOut;
    }

    public void setLineOut(Boolean lineOut) {
        this.lineOut = lineOut;
    }
}
