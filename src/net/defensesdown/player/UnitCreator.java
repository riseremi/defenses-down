package net.defensesdown.player;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 11:57
 */
public class UnitCreator {

    public static Entity getUnit(Entity.Type type, int x, int y, int ownerId) {
        switch (type) {
            case KNIGHT:
                return new Unit("/res/knight.png", ownerId, x, y, type);
            case RANGER:
                return new Unit("/res/ranger.png", ownerId, x, y, type);
            case TOWER:
                return new Unit("/res/tower.png", ownerId, x, y, type);
            default:
                return null;
        }
    }

}
