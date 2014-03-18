package net.defensesdown.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import net.defensesdown.player.GameClient;
import net.defensesdown.player.Unit;
import net.defensesdown.world.Tile;
import net.defensesdown.world.World;

/**
 * User: riseremi Date: 18.03.14 Time: 3:04
 */
public class Game extends JPanel implements KeyListener {
    public static final int WIDTH = 32 * 8;
    public static final int HEIGHT = 32 * 8;
    public static final int FRAME = 13;
    private World world;
    private ArrayList<Unit> units;
    private GameClient gameClient = new GameClient();
    private Mode mode;
    private final Rectangle cursor = new Rectangle(Tile.WIDTH, Tile.HEIGHT);
    int xx = FRAME;
    private boolean pressed = false;

    public Game() {
        try {
            world = new World("/res/tiles.png", 8, 8);
            world.setLayerPosition(FRAME, FRAME);
        } catch (IOException e) {
        }
        units = new ArrayList<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DOWN && !pressed) {
            cursor.y = cursor.y >= 32 * 7 ? 32 * 7 : cursor.y + 32;
            pressed = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && !pressed) {
            cursor.y = cursor.y <= 0 ? 0 : cursor.y - 32;
            pressed = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && !pressed) {
            cursor.x = cursor.x <= 0 ? 0 : cursor.x - 32;
            pressed = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && !pressed) {
            cursor.x = cursor.x >= 32 * 7 ? 32 * 7 : cursor.x + 32;
            pressed = true;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        world.paint(g);

        for (int i = 0; i < units.size(); i++) {
            units.get(i).paint(g);
        }

        g.setColor(Color.red);
        g.drawRect(cursor.x + FRAME, cursor.y + FRAME, cursor.width, cursor.height);

        g.drawLine(cursor.x + FRAME, cursor.y + FRAME, cursor.width + cursor.x + FRAME,
                cursor.height + cursor.y + FRAME);
        g.drawLine(cursor.x + FRAME + cursor.width, cursor.y + FRAME, cursor.x + FRAME,
                cursor.height + cursor.y + FRAME);
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public enum Mode {
        WAIT, SELECT, PLACE_UNIT
    }

    public Rectangle getCursorRect() {
        return cursor;
    }

}
