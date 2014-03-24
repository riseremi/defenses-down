package net.defensesdown.framework.network.messages;

/**
 * User: riseremi Date: 18.03.14 Time: 12:05
 */
public class MessageDealDamage extends Message {

    private final int damage, id;

    public MessageDealDamage(int id, int damage) {
        super(Type.DEAL_DAMAGE);
        this.damage = damage;
        this.id = id;
    }

    public int getDamage() {
        return damage;
    }

    public int getId() {
        return id;
    }
}
