package net.defensesdown.framework.network.messages;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 15:37
 */
public class MessageSetPlayerId extends Message {
    private final int id;

    public MessageSetPlayerId(int id) {
        super(Type.SET_PLAYER_ID);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
