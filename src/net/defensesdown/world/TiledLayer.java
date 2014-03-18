package net.defensesdown.world;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 2:52
 */
public class TiledLayer extends Layer {

    private final Tile[][] map;//[x][y]
    private final int[][] visiblity;//[x][y]
    private final BufferedImage[] tiles;
    private final int tileWidth, tileHeight;
    private final int horizontalTilesNumber, verticalTilesNumber;
    private final int paintWidth, paintHeight;

    public TiledLayer(BufferedImage image, int tileWidth, int tileHeight,
                      int width, int height, int paintWidth, int paintHeight) {
        super(width * tileWidth, height * tileHeight);
        if (image.getWidth() / tileWidth * tileWidth != image.getWidth()
                || image.getHeight() / tileHeight * tileHeight != image.getHeight()) {
            throw new IllegalArgumentException();
        }
        this.paintWidth = paintWidth;
        this.paintHeight = paintHeight;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        tiles = chopImage(image);
        map = new Tile[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = new Tile(0, true);
            }
        }

        visiblity = new int[width][height];
        horizontalTilesNumber = width;
        verticalTilesNumber = height;
    }

    public int getHorizontalTilesNumber() {
        return horizontalTilesNumber;
    }

    public int getVerticalTilesNumber() {
        return verticalTilesNumber;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Tile[][] getMap() {
        return map;
    }

    private BufferedImage[] chopImage(BufferedImage image) {
        int x = 0, y = 0;
        List<BufferedImage> list = new ArrayList<>();
        try {
            while (true) {
                while (true) {
                    BufferedImage subImage = image.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                    list.add(subImage);
                    x++;
                    if ((x + 1) * tileWidth > image.getWidth()) {
                        x = 0;
                        break;
                    }
                }
                y++;
                if ((y + 1) * tileHeight > image.getHeight()) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(x);
            System.out.println(y);
        }
        return list.toArray(new BufferedImage[list.size()]);
    }

    public void setTileObject(int x, int y, Tile tile) {
        map[x][y] = tile;
    }

    public void setTile(int x, int y, int tileId) {
        map[x][y].setId(tileId);
    }

    public Tile getTile(int x, int y) {
        return map[x][y];
    }

    public int[][] getVisiblity() {
        return visiblity;
    }

    public Tile getTileObject(int x, int y) {
        return map[x][y];
    }

    public void setVisiblity(int x, int y, int state) {
        visiblity[x][y] = state;
    }

    public void fillRectTile(int x, int y, int w, int h, int tileId) {
        for (int i = y; i < y + h; i++) {
            for (int j = x; j < w + x; j++) {
                setTile(j, i, tileId);
            }
        }
    }

    //отрисовка слоя, при этом рисуются только помещающиеся на экран тайлы
    @Override
    protected void paintLayer(Graphics g) {
        for (int i = 0; i < paintWidth; i++) {
            for (int j = 0; j < paintHeight; j++) {
                //try {
                paintTile(g, i * tileWidth, j * tileHeight, map[i - (getBlocksX())][j - (getBlocksY())].getId());
                //} catch (Exception ex) {
                //}
            }
        }
    }

    protected void paintTile(Graphics g, int x, int y, int id) {
        g.drawImage(tiles[id], x, y, null);
    }

}

