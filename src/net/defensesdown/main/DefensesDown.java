package net.defensesdown.main;

import net.defensesdown.screens.Game;
import net.defensesdown.screens.Lobby;

import javax.swing.*;
import java.awt.*;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 1:23
 */
public class DefensesDown extends JFrame {
    private static Game game;
    private static DefensesDown core;
    private static Lobby lobby;

    public DefensesDown() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Defenses Down");

        Dimension preferredSize = new Dimension(Game.WIDTH + Game.FRAME * 2, Game.HEIGHT + Game.FRAME * 2);
        this.getContentPane().setPreferredSize(preferredSize);
        this.setResizable(false);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dimension.width / 2 - Game.WIDTH / 2, dimension.height / 2 - Game.HEIGHT / 2);

    }

    public static void main(String[] args) {
        game = new Game();
        core = new DefensesDown();
        lobby = new Lobby();

        core.add(lobby);

        core.setVisible(true);
        core.pack();


    }

    public static Game getGame() {
        return game;
    }

    public static Lobby getLobby() {
        return lobby;
    }

    public static void startGame() {
        core.remove(lobby);
        core.add(game);
        core.revalidate();

    }
}
