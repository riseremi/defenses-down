package net.defensesdown.screens;

import net.defensesdown.player.GameClient;
import net.defensesdown.player.Unit;
import net.defensesdown.world.World;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 3:04
 */
public class Game extends JPanel {
    public static final int WIDTH = 32 * 8;
    public static final int HEIGHT = 32 * 8;
    public static final int FRAME = 13;
    private World world;
    private ArrayList<Unit> units;
    private GameClient gameClient;

    public Game() {
        try {
            world = new World("/res/tiles.png", 8, 8);
            world.setLayerPosition(FRAME, FRAME);
        } catch (IOException e) {
        }
        units = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        world.paint(g);

        for (int i = 0; i < units.size(); i++) {
            units.get(i).paint(g);
        }
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }
}
