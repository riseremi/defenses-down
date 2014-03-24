package net.defensesdown.framework.network.messages;

import java.io.Serializable;

/**
 * User: riseremi Date: 18.03.14 Time: 2:46
 */
public class Message implements Serializable {

    private final Type type;

    public Message(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {

        VOID, CONNECTION, SET_POSITION, CREATE_UNIT, SWAP_TEAMS, START_GAME,
        SET_PLAYER_ID, CREATE_GAME_CLIENT, END_TURN, DEAL_DAMAGE,
    }
}
