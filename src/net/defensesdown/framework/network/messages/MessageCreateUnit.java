package net.defensesdown.framework.network.messages;

import net.defensesdown.player.Entity;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 14:53
 */
public class MessageCreateUnit extends Message {
    private final Entity.Type type;
    private final int id, x, y;

    public MessageCreateUnit(Entity.Type type, int x, int y, int id) {
        super(Type.CREATE_UNIT);
        this.type = type;
        this.id = id;
        this.y = y;
        this.x = x;
    }

    public Entity.Type getUnitType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
