package net.defensesdown.world;

import java.awt.*;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 2:55
 */


public abstract class Layer {

    private final int width, height;
    private int x, y;
    private int shift;

    public Layer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final void paintComponent(Graphics g) {
        paintLayer(g);
    }

    public void paint(Graphics g) {
        g.translate(x, y);
        paintLayer(g);
        g.translate(-x, -y);
    }

    public void paint(Graphics g, int dx, int dy) {
        g.translate(x - dx, y - dy);
        paintLayer(g);
        g.translate(-(x - dx), -(y - dy));
    }

    protected abstract void paintLayer(Graphics g);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBlocksX() {
        return (x + shift) / Tile.WIDTH;
    }

    public void setBlocksX(int x) {
        this.x = x * Tile.WIDTH;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getBlocksY() {
        return (y + shift) / Tile.HEIGHT;
    }

    public void setBlocksY(int y) {
        this.y = y * Tile.HEIGHT;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
