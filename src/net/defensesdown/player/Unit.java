package net.defensesdown.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.defensesdown.screens.Game;
import net.defensesdown.world.Tile;

/**
 * User: riseremi Date: 18.03.14 Time: 1:39
 */
public class Unit extends Entity {
    private BufferedImage sprite;

    public Unit(String pathToSprite, int ownerId, int id, int x, int y, Type type) {
        super(type, ownerId, id);
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
//        System.out.println("" + getX() + ":" + getY() + ":" + getOwner());

        final int x = getX() * Tile.WIDTH + Game.FRAME;
        final int y = getY() * Tile.HEIGHT + Game.FRAME;

        g.drawImage(sprite, x, y, null);

        //Graphics g2 = sprite.getGraphics();
        if (getOwner() == GameClient.BLACK) {
            g.setColor(new Color(0, 0, 0));
        } else if (getOwner() == GameClient.WHITE) {
            g.setColor(new Color(255, 255, 255));
        }
        g.drawRect(x, y, Tile.WIDTH, Tile.HEIGHT);
        g.drawRect(x + 1, y + 1, Tile.WIDTH - 2, Tile.HEIGHT - 2);
//        g.drawRect(x + 2, y + 2, Tile.WIDTH - 4, Tile.HEIGHT - 4);

    }

    @Override
    public void update() {
    }

    @Override
    public String toString() {
        return getType().name() + " " + getHp() + " " + getOwner();
    }

}
