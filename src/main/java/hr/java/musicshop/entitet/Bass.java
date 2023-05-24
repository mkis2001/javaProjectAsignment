package hr.java.musicshop.entitet;

public class Bass extends Zicani{
    private boolean preamp;
    private BassShape shape;
    private String pickupConfig;
    public Bass(Long id, String slikaLink,  String proizvodac, String naziv, int cijena, String finish, String color, int brojZica, Number scaleLenght, int brPragova, String bodyWood, String neckWood, String fretboardWood, String shape, String pickupConfig, boolean preamp) {
        super(id, slikaLink, proizvodac, naziv, TipInstrumenta.ELEKTRICNI, cijena, finish, color, brojZica, scaleLenght, brPragova, bodyWood, neckWood, fretboardWood, shape);
        this.pickupConfig = pickupConfig;
        this.preamp = preamp;
        switch (shape){
            case "Jazz" -> this.shape = BassShape.JAZZ;
            case "P shape" -> this.shape = BassShape.P;
            case "Alternative" -> this.shape = BassShape.ALTERNATIVE;
        }
    }

    public Boolean getPreamp() {
        return preamp;
    }

    public void setPreamp(boolean preamp) {
        this.preamp = preamp;
    }

    public String getPickupConfig() {
        return pickupConfig;
    }

    public void setPickupConfig(String pickupConfig) {
        this.pickupConfig = pickupConfig;
    }

    @Override
    public BassShape getShape() {
        return shape;
    }
}
