package net.defensesdown.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import net.defensesdown.framework.network.Client;
import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.framework.network.messages.MessageDealDamage;
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

    public static boolean KOSTYL = true;
    public static final int BWIDTH = 8, BHEIGHT = 8;
    public static int SIDEBAR_WIDTH, SIDEBAR_HEIGHT;
    public static int GWIDTH;
    public static int GHEIGHT;
    public static int FRAME;
    private World world;
    private final ArrayList<Unit> units;
    private final GameClient gameClient = new GameClient();
    private Mode mode = Mode.SELECT;
    private final Rectangle cursor;
    private boolean pressed = false;
    private final Color SELECTION_COLOR = Color.RED, PLACE_COLOR = Color.BLUE;
    private Color currentColor = SELECTION_COLOR;
    private Unit selectedUnit;
    private TiledLayer paper;
    private BufferedImage paperImg, paperImgLarge;
    private boolean myTurn;
    private final static String TILES_NORMAL = "/res/tiles.png";
    private final static String TILES_HD = "/res/tilesHD.png";
    private String turnText;
    private boolean drawTurnText;

    public boolean isDrawTurnText() {
        return drawTurnText;
    }

    public void setDrawTurnText(boolean drawTurnText) {
        this.drawTurnText = drawTurnText;
    }

    public String getTurnText() {
        return turnText;
    }

    public void setTurnText(String turnText) {
        this.turnText = turnText;
    }

    public Game(boolean isHD) {
        Tile.init(isHD);
        FRAME = isHD ? 26 : 13;
        SIDEBAR_WIDTH = BWIDTH / 2 * Tile.WIDTH;
        GWIDTH = Tile.WIDTH * BWIDTH + FRAME + SIDEBAR_WIDTH;
        GHEIGHT = Tile.HEIGHT * BHEIGHT;
        SIDEBAR_HEIGHT = GHEIGHT;

        String pathToTileset = isHD ? TILES_HD : TILES_NORMAL;
        cursor = new Rectangle(Tile.WIDTH, Tile.HEIGHT);

        try {
            world = new World(pathToTileset, BWIDTH, BHEIGHT);
            world.setLayerPosition(FRAME, FRAME);
            paper = new TiledLayer(ImageIO.read(Unit.class.getResourceAsStream(pathToTileset)), Tile.WIDTH, Tile.HEIGHT, BWIDTH / 2, BHEIGHT, BWIDTH, BHEIGHT);
            paper.fillRectTile(0, 0, paper.getHorizontalTilesNumber(), paper.getVerticalTilesNumber(), 3);
            paper.setY(FRAME);

            paperImg = ImageIO.read(Unit.class.getResourceAsStream("/res/paper.png"));
            paperImgLarge = new BufferedImage(SIDEBAR_WIDTH, SIDEBAR_HEIGHT, BufferedImage.TYPE_INT_RGB);

            Graphics g = paperImgLarge.getGraphics();

            for (int i = 0; i < BWIDTH / 2; i++) {
                for (int j = 0; j < BHEIGHT; j++) {
                    g.drawImage(paperImg.getScaledInstance(Tile.WIDTH, Tile.HEIGHT, 3), i * Tile.WIDTH, j * Tile.HEIGHT, this);
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

        //kostyl + secret damage button (BWGHAHAHAHAHAH OAK YOU ARE THE LIVING DEAD)
        if (e.getKeyCode() == KeyEvent.VK_F && e.isAltDown() && e.isShiftDown()) {
            mode = Mode.SELECT;
            currentColor = SELECTION_COLOR;
            pressed = true;
            if (selectedUnit != null) {
                selectedUnit.dealDamage(10);
            }
            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (mode == Mode.SELECT) {
                if (getSelectedUnit(cursor.x, cursor.y) != null
                        && getSelectedUnit(cursor.x, cursor.y).isOwned()) {
                    mode = Mode.PLACE_UNIT;
                    currentColor = PLACE_COLOR;
                    pressed = true;
                    selectedUnit = getSelectedUnit(cursor.x, cursor.y);
                    repaint();
                }

            } else if (mode == Mode.PLACE_UNIT) {
                final int x = selectedUnit.getX();
                final int y = selectedUnit.getY();
                if (getSelectedUnit(cursor.x, cursor.y) == null || (getSelectedUnit(cursor.x, cursor.y).getX() == x
                        && (getSelectedUnit(cursor.x, cursor.y).getY() == y))) {
                    try {
                        System.out.println();
                        if (selectedUnit.isCellAvailable(cursor.x, cursor.y, x, y)) {
                            mode = Mode.SELECT;
                            currentColor = SELECTION_COLOR;
                            pressed = true;

                            if (cursor.x != x || cursor.y != y) {
                                Message msg = new MessageSetPosition(cursor.x, cursor.y, selectedUnit.getId());
                                Client.getInstance().send(msg);

                                msg = new MessageEndTurn();
                                Client.getInstance().send(msg);

                                DefensesDown.getFrames()[0].setTitle("Defenses Down - Enemy turn");
                                setTurnText("ENEMY TURN");
                                setDrawTurnText(true);

                                myTurn = false;
                            }
                        }
                    } catch (IOException ex) {
                    }
                    repaint();
                } else if (getSelectedUnit(cursor.x, cursor.y) != null) {
                    Unit temp = getSelectedUnit(cursor.x, cursor.y);
                    if (!temp.isOwned()) {
                        try {
                            Message msg = new MessageDealDamage(temp.getId(), temp.getAttack());
                            Client.getInstance().send(msg);

                            mode = Mode.SELECT;
                            currentColor = SELECTION_COLOR;
                            pressed = true;

                            msg = new MessageEndTurn();
                            Client.getInstance().send(msg);

                            DefensesDown.getFrames()[0].setTitle("Defenses Down - Enemy turn");
                            myTurn = false;
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        }
    }

    public void drawTest(String text, int x, int y, Graphics g2) throws InterruptedException {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int width = Game.GWIDTH + Game.FRAME * 2;
        Font fontOld = g.getFont();
        Font font = new Font("Verdana", Font.PLAIN, 36);
        g.setFont(font);
        int textWidth = g.getFontMetrics(font).stringWidth(text);
        int textHeight = g.getFontMetrics(font).getHeight() / 3 * 4; //supah kostyl needs to center vertical align
        int centerX = width / 2 - textWidth / 2;

        g.setColor(new Color(0, 0, 0, 255));
        g.fillRect(x, y, width, 96);
        g.setColor(new Color(255, 255, 255, 255));
        g.drawString(text, centerX, y + textHeight);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed = false;
    }

    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        world.paint(g);

        if (Game.KOSTYL) {
            for (Unit unit : units) {
                unit.paint(g);
            }
        }
        if (mode == Mode.SELECT) {
            selectedUnit = getSelectedUnit(cursor.x, cursor.y);
        }

        if (mode == Mode.PLACE_UNIT) {
            selectedUnit.drawMovement(selectedUnit.getAppliedMovementScheme(selectedUnit.getX(), selectedUnit.getY()), g);
        }

        int x = GWIDTH - SIDEBAR_WIDTH + FRAME;
        int pref = 16;

        g.drawImage(paperImgLarge, x, FRAME, this);

        g.setColor(Color.WHITE);

        if (selectedUnit != null) {
            g.drawString(selectedUnit.getType().name(), x, FRAME + 16);
            //stats
            g.drawString("HP: " + selectedUnit.getHp() + "/" + selectedUnit.getMaxHp(), x, FRAME + 16 + pref * 2);
            g.drawString("DEF: " + selectedUnit.getDef(), x, FRAME + 16 + pref * 3);
            g.drawString("ATK: " + selectedUnit.getAttack(), x, FRAME + 16 + pref * 4);
            g.drawString("BOOST: " + selectedUnit.getBoost() + "/" + Entity.BOOST_MAX, x, FRAME + 16 + pref * 5);

            g.drawString("MOVE SCHEME:", x, FRAME + 16 + pref * 7);
            g.drawImage(selectedUnit.getMoveScheme(), x, FRAME + 16 + pref * 7 + 8, this);
        } else {
            g.drawString("Select a unit", x + pref / 16, FRAME + pref);

        }
        g.setColor(currentColor);
        g.drawRect(cursor.x * Tile.WIDTH + FRAME, cursor.y * Tile.HEIGHT + FRAME, cursor.width, cursor.height);
        g.drawRect(cursor.x * Tile.WIDTH + FRAME + 2, cursor.y * Tile.HEIGHT + FRAME + 2, cursor.width - 4, cursor.height - 4);

        if (isDrawTurnText()) {
            try {
//                drawTest(turnText, 0, 96, g2);
                g.drawString(turnText, FRAME + pref, FRAME + pref);
                repaint();
                Thread.sleep(1000L);
                setDrawTurnText(false);
            } catch (InterruptedException ex) {
            }
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

    private Unit getSelectedUnit(int x, int y) {
        Game.KOSTYL = false;
        for (Unit unit : units) {
            if (unit.getX() == x && unit.getY() == y && unit.isEnabled()) {
                Game.KOSTYL = true;
                return unit;
            }
        }
        Game.KOSTYL = true;
        return null;
    }

//    public void removeUnitById(int id) {
//        for (Unit unit : units) {
//            if (unit.getId() == id) {
//                units.remove(unit);
//            }
//        }
//    }
}
