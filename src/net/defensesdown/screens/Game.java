package net.defensesdown.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import net.defensesdown.framework.network.Client;
import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.framework.network.messages.MessageEndTurn;
import net.defensesdown.framework.network.messages.MessageSetPosition;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.player.Entity;
import net.defensesdown.player.GameClient;
import net.defensesdown.player.Unit;
import net.defensesdown.world.Tile;
import net.defensesdown.world.TiledLayer;
import net.defensesdown.world.World;

/**
 * User: riseremi Date: 18.03.14 Time: 3:04
 */
public class Game extends JPanel implements KeyListener {

    public static final int BWIDTH = 8, BHEIGHT = 8;
    public static final int GWIDTH = Tile.WIDTH * BWIDTH;
    public static final int GHEIGHT = Tile.HEIGHT * BHEIGHT;
    public static final int FRAME = 13;
    private World world;
    private final ArrayList<Unit> units;
    private final GameClient gameClient = new GameClient();
    private Mode mode = Mode.SELECT;
    private final Rectangle cursor = new Rectangle(Tile.WIDTH, Tile.HEIGHT);
    int xx = FRAME;
    private boolean pressed = false;
    private final Color SELECTION_COLOR = Color.RED, PLACE_COLOR = Color.BLUE;
    private Color currentColor = SELECTION_COLOR;
    private Unit selectionUnit;
    private TiledLayer paper;
    private BufferedImage paperImg, paperImgLarge;
    private boolean myTurn;

    public Game() {
        try {
            world = new World("/res/tiles.png", BWIDTH, BHEIGHT);
            world.setLayerPosition(FRAME, FRAME);
            paper = new TiledLayer(ImageIO.read(Unit.class.getResourceAsStream("/res/tiles.png")), Tile.WIDTH, Tile.HEIGHT, BWIDTH / 2, BHEIGHT, BWIDTH, BHEIGHT);
            paper.fillRectTile(0, 0, paper.getHorizontalTilesNumber(), paper.getVerticalTilesNumber(), 3);
            paper.setY(FRAME);

            paperImg = ImageIO.read(Unit.class.getResourceAsStream("/res/paper.png"));
            paperImgLarge = new BufferedImage(BWIDTH / 2 * Tile.WIDTH, BHEIGHT * Tile.HEIGHT, BufferedImage.TYPE_INT_RGB);

            Graphics g = paperImgLarge.getGraphics();

            for (int i = 0; i < BWIDTH / 2; i++) {
                for (int j = 0; j < BHEIGHT; j++) {
                    g.drawImage(paperImg, i * Tile.WIDTH, j * Tile.HEIGHT, this);
                }

            }
        } catch (IOException e) {
        }
        units = new ArrayList<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressed && myTurn) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    cursor.y = cursor.y >= BHEIGHT - 1 ? BHEIGHT - 1 : cursor.y + 1;
                    pressed = true;
                    break;
                case KeyEvent.VK_UP:
                    cursor.y = cursor.y <= 0 ? 0 : cursor.y - 1;
                    pressed = true;
                    break;
                case KeyEvent.VK_LEFT:
                    cursor.x = cursor.x <= 0 ? 0 : cursor.x - 1;
                    pressed = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    cursor.x = cursor.x >= BWIDTH - 1 ? BWIDTH - 1 : cursor.x + 1;
                    pressed = true;
                    break;
            }
            repaint();
        }

        //kostyl
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            mode = Mode.SELECT;
            currentColor = SELECTION_COLOR;
            pressed = true;
            repaint();
        }

        if (mode == Mode.SELECT) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (getSelectionUnit(cursor.x, cursor.y) != null
                        && getSelectionUnit(cursor.x, cursor.y).getOwner()
                        == getGameClient().getFraction()) {
                    mode = Mode.PLACE_UNIT;
                    currentColor = PLACE_COLOR;
                    pressed = true;
                    selectionUnit = getSelectionUnit(cursor.x, cursor.y);
                    repaint();
                }
            }
        } else if (mode == Mode.PLACE_UNIT) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (getSelectionUnit(cursor.x, cursor.y) == null
                        || (getSelectionUnit(cursor.x, cursor.y).getX() == selectionUnit.getX() / Tile.WIDTH
                        && (getSelectionUnit(cursor.x, cursor.y).getY() == selectionUnit.getY() / Tile.HEIGHT))) {
                    try {
                        if (selectionUnit.isCellAvailable(cursor.x, cursor.y, selectionUnit.getX(), selectionUnit.getY())) {
                            mode = Mode.SELECT;
                            currentColor = SELECTION_COLOR;
                            pressed = true;

                            Message msg = new MessageSetPosition(cursor.x, cursor.y, selectionUnit.getId());
                            Client.getInstance().send(msg);

                            msg = new MessageEndTurn();
                            Client.getInstance().send(msg);

                            DefensesDown.getFrames()[0].setTitle("Defenses Down - Enemy turn");
                            myTurn = false;
                        }
                    } catch (IOException ex) {
                    }
                    repaint();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        world.paint(g);

        for (Unit unit : units) {
            unit.paint(g);
        }

        final Unit selectionUnit1 = getSelectionUnit(cursor.x, cursor.y);

        g.setColor(currentColor);
        g.drawRect(cursor.x * Tile.WIDTH + FRAME, cursor.y * Tile.HEIGHT + FRAME, cursor.width, cursor.height);

        if (selectionUnit1 != null) {
            g.setColor(Color.WHITE);
            int x = cursor.x > 3 ? FRAME : GWIDTH / 2 + FRAME + 8;
            g.drawImage(paperImgLarge, cursor.x > 3 ? FRAME : GWIDTH / 2 + FRAME, FRAME, this);

            int pref = 16;

            g.drawString(selectionUnit1.getType().name(), x, FRAME + 16);
            //stats
            g.drawString("HP: " + selectionUnit1.getHp() + "/" + selectionUnit1.getMaxHp(), x, FRAME + 16 + pref * 2);
            g.drawString("DEF: " + selectionUnit1.getDef(), x, FRAME + 16 + pref * 3);
            g.drawString("ATK: " + selectionUnit1.getAttack(), x, FRAME + 16 + pref * 4);
            g.drawString("BOOST: " + selectionUnit1.getBoost() + "/" + Entity.BOOST_MAX, x, FRAME + 16 + pref * 5);

            g.drawString("MOVE SCHEME:", x, FRAME + 16 + pref * 7);
            g.drawImage(selectionUnit1.getMoveScheme(), x, FRAME + 16 + pref * 7 + 8, this);
        }
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

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    private Unit getSelectionUnit(int x, int y) {
        for (Unit unit : units) {
            if (unit.getX() == x) {
                if (unit.getY() == y) {
                    return unit;
                }
            }
        }
        return null;
    }

}
