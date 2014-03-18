package net.defensesdown.components;

import java.awt.*;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 1:42
 */
public abstract class DrawableGameComponent extends GameComponent {
    protected int x, y;

    public abstract void paint(Graphics g);
}
