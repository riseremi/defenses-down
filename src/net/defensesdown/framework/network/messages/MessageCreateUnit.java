package net.defensesdown.framework.network.messages;

import net.defensesdown.player.Entity;

/**
 * User: riseremi Date: 18.03.14 Time: 14:53
 */
public class MessageCreateUnit extends Message {
    private final Entity.Type type;
    private final int ownerId, id, x, y;

    public MessageCreateUnit(Entity.Type type, int x, int y, int id, int ownerId) {
        super(Type.CREATE_UNIT);
        this.type = type;
        this.ownerId = ownerId;
        this.id = id;
        this.y = y;
        this.x = x;
    }

    public Entity.Type getUnitType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
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
