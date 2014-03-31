package net.defensesdown.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import net.defensesdown.main.DefensesDown;
import net.defensesdown.screens.Game;

/**
 *
 * @author remi
 */
public class GraphicsUtils implements Runnable {

    private static String text;
    private static int x, y;
    private static Graphics2D g;
    private static boolean drawTable;

//    public static void drawTest(String text, int x, int y, Graphics g2) throws InterruptedException {
//        Graphics2D g = (Graphics2D) g2;
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        final int width = Game.GWIDTH + Game.FRAME * 2;
//        Font fontOld = g.getFont();
//        Font font = new Font("Verdana", Font.PLAIN, 36);
//        g.setFont(font);
//        int textWidth = g.getFontMetrics(font).stringWidth(text);
//        int textHeight = g.getFontMetrics(font).getHeight() / 3 * 4; //supah kostyl needs to center vertical align
//        int centerX = width / 2 - textWidth / 2;
//
//        g.setColor(new Color(0, 0, 0, 255));
//        g.fillRect(x, y, width, 96);
//        g.setColor(new Color(255, 255, 255, 255));
//        g.drawString(text, centerX, y + textHeight);
//        DefensesDown.getGame().repaint();
//
//        Thread.sleep(1000L);
//    }
    
    public static void draw(String text, int x, int y, Graphics g2) {
        GraphicsUtils.g = (Graphics2D) g2;
        GraphicsUtils.text = text;
        GraphicsUtils.x = x;
        GraphicsUtils.y = y;
        drawTable = true;
    }

    @Override
    public void run() {
        while (true) {

            if (drawTable) {
                System.out.println("table drawn");

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                final int width = Game.GWIDTH + Game.FRAME * 2;
                Font fontOld = g.getFont();
                Font font = new Font("Verdana", Font.PLAIN, 36);
                g.setFont(font);
                int textWidth = g.getFontMetrics(font).stringWidth(text);
                int textHeight = g.getFontMetrics(font).getHeight() / 3 * 4; //supah kostyl needs to center vertical align
                int centerX = width / 2 - textWidth / 2;

                g.setColor(new Color(0, 0, 0));
                g.fillRect(x, y, width, 96);
                g.setColor(new Color(255, 255, 255));
                g.drawString(text, centerX, y + textHeight);
                DefensesDown.getGame().repaint();

                try {
                    Thread.sleep(1000L);
                    drawTable = false;
                } catch (InterruptedException ex) {
                }
            }
        }
    }

}
