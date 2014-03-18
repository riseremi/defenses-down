package net.defensesdown.player;

import net.defensesdown.components.DrawableGameComponent;
import net.defensesdown.world.Tile;

/**
 * User: riseremi Date: 18.03.14 Time: 1:38
 */
public abstract class Entity extends DrawableGameComponent {
    public static final int BOOST_MAX = 10;
    private int hp, def, boost;
    private int x, y;
    private Type type;
    private int owner;
    private MovingStyle movingStyle;

    public Entity(Type type, int ownerId) {
        this.owner = ownerId;
        this.type = type;
        this.setStats(StatsBatch.getStatsFor(type));
    }

    public void setStats(StatsBatch statsBatch) {
        this.setHp(statsBatch.getHp());
        this.setDef(statsBatch.getDef());
        this.setMovingStyle(statsBatch.getMovingStyle());
    }

    public int getHp() {
        return hp;
    }

    private void setHp(int hp) {
        this.hp = hp;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getOwner() {
        return owner;
    }

//    public void setOwner(int owner) {
//        this.owner = owner;
//    }

    public void setX(int x) {
        this.x = x;
    }

    public void setMovingStyle(MovingStyle movingStyle) {
        this.movingStyle = movingStyle;
    }

    public int getDef() {
        return def;
    }

    private void setDef(int def) {
        this.def = def;
    }

    public void moveUp() {
        y = -Tile.HEIGHT;
    }

    public void moveDown() {
        y = +Tile.HEIGHT;
    }

    public void moveLeft() {
        x = -Tile.WIDTH;
    }

    public void moveRight() {
        x = +Tile.WIDTH;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        VOID, KNIGHT, RANGER, TOWER
    }

    public enum Owner {
        PLAYER, ENEMY
    }

    public enum MovingStyle {
        PAWN, PAWN_WITH_DIAGONAL, NO_MOVE
    }
}
