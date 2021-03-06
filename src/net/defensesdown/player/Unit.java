package net.defensesdown.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.defensesdown.framework.network.Client;
import net.defensesdown.framework.network.Server;
import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.framework.network.messages.MessageDealDamage;
import net.defensesdown.framework.network.messages.MessageDeleteUnit;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.screens.Game;
import net.defensesdown.world.Tile;

/**
 * User: riseremi Date: 18.03.14 Time: 1:39
 */
public class Unit extends Entity {

    public static final int[] KNIGHT_MS = {
        0, 1, 0,
        1, 1, 1,
        0, 1, 0
    };

    public static final int[] RANGER_MS = {
        1, 1, 1,
        1, 1, 1,
        1, 1, 1
    };

    public static final int[] TOWER_MS = {
        0, 0, 0,
        0, 1, 0,
        0, 0, 0
    };

    private BufferedImage sprite;
    private BufferedImage moveScheme;
    private int[] movementScheme;

    public Unit(String pathToSprite, int ownerId, int id, int x, int y, int attackRadius, Type type) {
        super(type, ownerId, id);
        setX(x);
        setY(y);
        setAttackRadius(attackRadius);

        switch (type) {
            case KNIGHT:
                movementScheme = KNIGHT_MS;
                break;
            case RANGER:
                movementScheme = RANGER_MS;
                break;
            case TOWER:
                movementScheme = TOWER_MS;
                break;
        }
        try {
            sprite = ImageIO.read(Unit.class.getResourceAsStream(pathToSprite));
            moveScheme = ImageIO.read(Unit.class.getResourceAsStream("/res/schemes/" + type.name().toLowerCase() + ".png"));
        } catch (IOException e) {
            System.out.println("Cannot load image.");
        }
    }

    @Override
    public void paint(Graphics g) {
        if (isEnabled()) {
            final int ux2 = getX() * Tile.WIDTH + (Tile.WIDTH / 2 - sprite.getWidth() / 2) + Game.FRAME;
            final int uy2 = getY() * Tile.HEIGHT + (Tile.HEIGHT / 2 - sprite.getHeight() / 2) + Game.FRAME;

            final int ux = getX() * Tile.WIDTH + Game.FRAME;
            final int uy = getY() * Tile.HEIGHT + Game.FRAME;

            g.drawImage(sprite, ux2, uy2, null);

            if (getOwner() == GameClient.BLACK) {
                g.setColor(new Color(0, 0, 0));
            } else if (getOwner() == GameClient.WHITE) {
                g.setColor(new Color(255, 255, 255));
            }
            g.drawRect(ux, uy, Tile.WIDTH, Tile.HEIGHT);
            g.drawRect(ux + 2, uy + 2, Tile.WIDTH - 4, Tile.HEIGHT - 4);

            int hpWidth = getHp() * (Tile.WIDTH - 2) / getMaxHp();
            g.setColor(getHp() < getMaxHp() / 3 ? Color.RED : Color.GREEN);
            g.fillRect(ux + 1, uy + Tile.WIDTH - Tile.HEIGHT / 10, hpWidth, Tile.HEIGHT / 10);
        }
    }

    public BufferedImage getMoveScheme() {
        return moveScheme;
    }

    public int[][] getAppliedMovementScheme(int hx, int hy) {
        int[][] map = new int[Game.BWIDTH][Game.BHEIGHT];
        setValue(map, hx - 1, hy - 1, movementScheme[0]);
        setValue(map, hx, hy - 1, movementScheme[1]);
        setValue(map, hx + 1, hy - 1, movementScheme[2]);

        setValue(map, hx - 1, hy, movementScheme[3]);
        setValue(map, hx, hy, movementScheme[4]);
        setValue(map, hx + 1, hy, movementScheme[5]);

        setValue(map, hx - 1, hy + 1, movementScheme[6]);
        setValue(map, hx, hy + 1, movementScheme[7]);
        setValue(map, hx + 1, hy + 1, movementScheme[8]);

        return map;
    }

    public boolean isCellAvailable(int cx, int cy, int hx, int hy) {
        return getAppliedMovementScheme(hx, hy)[cx][cy] == 1;
    }

    public void drawMovement(int[][] m, Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                if (m[i][j] != 0) {
                    g.fillRect(i * Tile.WIDTH + Game.FRAME, j * Tile.HEIGHT + Game.FRAME, Tile.WIDTH, Tile.HEIGHT);
                }
            }
        }
    }

    private void setValue(int[][] map, int x, int y, int value) {
        if (x >= 0 && y >= 0 && x <= Game.BWIDTH - 1 && y <= Game.BHEIGHT - 1) {
            map[x][y] = value;
        }
    }

    @Override
    public void update() {
    }

    public boolean isOwned() {
        return getOwner() == DefensesDown.getGame().getGameClient().getFraction();
    }

    @Override
    public String toString() {
        return getType().name() + " " + getHp() + " " + getOwner();
    }

    public void dealPureDamage(int damage) {
        final boolean isDead = getHp() - damage <= 0;
        setHp(isDead ? 0 : getHp() - damage);
        if (getHp() == 0) {
            try {
                Message message = new MessageDeleteUnit(getId());
                final Server instance = Server.getInstance();
                if (instance == null) {
                    System.out.println("server is null");
                }
                instance.sendToAll(message);
            } catch (IOException ex) {
            }
        }
    }

    public void dealDamage(int damage) {
        try {
            Message msg = new MessageDealDamage(getId(), damage);
            Client.getInstance().send(msg);
        } catch (IOException ex) {
        }
    }

}
