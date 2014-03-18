package net.defensesdown.world;

import net.defensesdown.components.DrawableGameComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 2:51
 */
public class World extends DrawableGameComponent {
    private TiledLayer layer;
    private TiledLayer frame;

    public World(String path, int w, int h) throws IOException {
        layer = new TiledLayer(ImageIO.read(World.class.getResourceAsStream(path)), Tile.WIDTH, Tile.HEIGHT, w, h, w, h);
        frame = new TiledLayer(ImageIO.read(World.class.getResourceAsStream(path)), Tile.WIDTH, Tile.HEIGHT, ++w, ++h, w, h);

        for (int i = 1; i < 8; i += 2) {
            for (int j = 0; j < 8; j += 2) {
                layer.setTile(i, j, 1);

            }
        }
        //
        for (int i = 0; i < 8; i += 2) {
            for (int j = 1; j < 8; j += 2) {
                layer.setTile(i, j, 1);
            }
        }

        //frame layer
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                frame.setTile(i, j, 2);
            }
        }
    }

    public void setLayerPosition(int x, int y) {
        layer.setX(x);
        layer.setY(y);
    }

    @Override
    public void paint(Graphics g) {
        frame.paint(g);
        layer.paint(g);
    }

    @Override
    public void update() {
    }
}
