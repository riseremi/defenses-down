package net.defensesdown.framework.network.messages;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 12:05
 */
public class MessageSetPosition extends Message {
    private final int x, y, id;

    public MessageSetPosition(int x, int y, int id) {
        super(Type.SET_POSITION);
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}
