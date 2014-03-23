package net.defensesdown.world;

/**
 * User: riseremi Date: 18.03.14 Time: 2:57
 */
public class Tile {

    public static int WIDTH, HEIGHT;
    private int id;
    private boolean walkable;

    public Tile(int id, boolean walkable) {
        this.id = id;
        this.walkable = walkable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public static void init(boolean isHD) {
        if (isHD) {
            WIDTH = HEIGHT = 64;
        } else {
            WIDTH = HEIGHT = 32;
        }
    }
}
