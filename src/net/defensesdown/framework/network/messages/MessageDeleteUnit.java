package net.defensesdown.framework.network.messages;

/**
 *
 * @author remi
 */
public class MessageDeleteUnit extends Message {

    private final int id;

    public MessageDeleteUnit(int id) {
        super(Type.DELETE_UNIT);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
