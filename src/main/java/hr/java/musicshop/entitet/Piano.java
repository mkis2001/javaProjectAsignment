package hr.java.musicshop.entitet;

public class Piano extends Keys {
    private int weight;
    private int height;

    public Piano(Long id, String slikaLink, String proizvodac, String naziv, int cijena, String finishType, String color, int weight, int height) {
        super(id, slikaLink, proizvodac, naziv, cijena, TipInstrumenta.AKUSTICNI, finishType, color, 88);
        this.weight = weight;
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
