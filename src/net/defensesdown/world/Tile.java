package net.defensesdown.world;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 2:57
 */
public class Tile {
    public static final int WIDTH = 32, HEIGHT = 32;
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
}
