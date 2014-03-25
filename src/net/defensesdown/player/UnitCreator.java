package net.defensesdown.player;

/**
 * User: riseremi Date: 18.03.14 Time: 11:57
 */
public class UnitCreator {

    public static Entity getUnit(Entity.Type type, int x, int y, int id, int ownerId) {
        switch (type) {
            case KNIGHT:
                String[] knights = {"/res/knight-black.png", "/res/knight-white.png"};
                return new Unit(knights[ownerId], ownerId, id, x, y, type);
            case RANGER:
                String[] mage = {"/res/mage-white.png", "/res/mage-black.png"};
                return new Unit(mage[ownerId], ownerId, id, x, y, type);
            case TOWER:
                String[] tower = {"/res/tower-white.png", "/res/tower-black.png"};
                return new Unit(tower[ownerId], ownerId, id, x, y, type);
            default:
                return null;
        }
    }

}
