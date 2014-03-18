package net.defensesdown.controller;

import java.awt.event.KeyEvent;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.world.Tile;

/**
 *
 * @author Riseremi
 */
public class KeyboardController {

    public static void processKeys(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            DefensesDown.getGame().getCursorRect().y += Tile.HEIGHT;
        }
    }

}
