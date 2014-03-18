package net.defensesdown.player;

import net.defensesdown.screens.Game;
import net.defensesdown.world.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 1:39
 */
public class Unit extends Entity {
    private BufferedImage sprite;

    public Unit(String pathToSprite, int ownerId, int x, int y, Type type) {
        super(type, ownerId);
        setX(x);
        setY(y);
        try {
            sprite = ImageIO.read(Unit.class.getResourceAsStream(pathToSprite));
        } catch (IOException e) {
            System.out.println("Cannot load image.");
        }
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("" + getX() + ":" + getY());

        g.drawImage(sprite, getX() * Tile.WIDTH + Game.FRAME, getY() * Tile.HEIGHT + Game.FRAME, null);
    }

    @Override
    public void update() {
    }

    @Override
    public String toString() {
        return getType().name() + " " + getHp();
    }

}
