package net.defensesdown.components;

import java.io.Serializable;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 1:39
 */
public abstract class GameComponent implements Serializable {
    private boolean enabled;

    public abstract void update();

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

}
