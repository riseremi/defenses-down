package net.defensesdown.framework.network.messages;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 17:31
 */
public class MessageConnected extends Message {
    private final String name;
    private final int fraction;

    public MessageConnected(String name, int fraction) {
        super(Type.CONNECTION);
        this.name = name;
        this.fraction = fraction;
    }

    public String getName() {
        return name;
    }

    public int getFraction() {
        return fraction;
    }
}
