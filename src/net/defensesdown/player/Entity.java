package net.defensesdown.player;

import net.defensesdown.components.DrawableGameComponent;
import net.defensesdown.world.Tile;

/**
 * User: riseremi Date: 18.03.14 Time: 1:38
 */
public abstract class Entity extends DrawableGameComponent {

    public static final int BOOST_MAX = 10;
    private int hp, maxHp, def, attack, boost;
    private int x, y, id;
    private Type type;
    private int owner;
    private MovingStyle movingStyle;
    private int attackRadius;

    public Entity(Type type, int ownerId, int id) {
        this.owner = ownerId;
        this.type = type;
        this.id = id;
        this.setStats(StatsBatch.getStatsFor(type));
    }

    public final void setStats(StatsBatch statsBatch) {
        this.setHp(statsBatch.getHp());
        this.setDef(statsBatch.getDef());
        this.setAttack(statsBatch.getAttack());
        this.setMaxHp(statsBatch.getHp());
        this.setMovingStyle(statsBatch.getMovingStyle());
    }

    public int getAttackRadius() {
        return attackRadius;
    }

    public void setAttackRadius(int attackRadius) {
        this.attackRadius = attackRadius;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    protected void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
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

    public int getBoost() {
        return boost;
    }

    public void setBoost(int boost) {
        this.boost = boost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
