package net.defensesdown.player;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 11:39
 */
public class StatsBatch {
    private int hp, def;
    private Entity.MovingStyle movingStyle;

    public StatsBatch(int hp, int def, Entity.MovingStyle movingStyle) {
        this.hp = hp;
        this.def = def;
        this.movingStyle = movingStyle;
    }

    public static StatsBatch getStatsFor(Entity.Type type) {
        switch (type) {
            case KNIGHT:
                return new StatsBatch(30, 5, Entity.MovingStyle.PAWN);
            case RANGER:
                return new StatsBatch(20, 1, Entity.MovingStyle.PAWN_WITH_DIAGONAL);
            case TOWER:
                return new StatsBatch(50, 0, Entity.MovingStyle.NO_MOVE);
            default:
                return new StatsBatch(5000, 3000, Entity.MovingStyle.NO_MOVE);
        }
    }

    public int getHp() {
        return hp;
    }

    public int getDef() {
        return def;
    }

    public Entity.MovingStyle getMovingStyle() {
        return movingStyle;
    }


}
