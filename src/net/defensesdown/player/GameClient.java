package net.defensesdown.player;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 12:13
 */
public class GameClient {

    public static final int WHITE = 1, BLACK = 0;
    private String name;
    private int fraction, id; //black 0, white 1

    public GameClient(String name, int fraction) {
        this.name = name;
        this.fraction = fraction;
    }

    public String getName() {
        return name;
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String fractionString = fraction == 0 ? "BLACK" : "WHITE";
        return name + " (" + fractionString + ")";
    }
}
