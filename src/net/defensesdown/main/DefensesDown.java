package net.defensesdown.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.defensesdown.screens.Game;
import net.defensesdown.screens.Lobby;

/**
 * User: riseremi Date: 18.03.14 Time: 1:23
 */
public class DefensesDown extends JFrame {

    private static Game game;
    private static DefensesDown core;
    private static Lobby lobby;

    public DefensesDown() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Defenses Down");

        System.out.println("Running with resolution: " + Game.GWIDTH + "x" + Game.GHEIGHT);

        Dimension preferredSize = new Dimension(Game.GWIDTH + Game.FRAME * 2, Game.GHEIGHT + Game.FRAME * 2);
        this.getContentPane().setPreferredSize(preferredSize);
        this.setResizable(false);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dimension.width / 2 - Game.GWIDTH / 2, dimension.height / 2 - Game.GHEIGHT / 2);
    }

    public static void main(String[] args) {
        boolean isHD = false;

        if (args.length > 0 && args[0].equals("hd")) {
            isHD = true;
        }

        game = new Game(isHD);
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
        core.addKeyListener(game);
        core.requestFocus();

    }
}
