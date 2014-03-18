package net.defensesdown.framework;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * User: riseremi
 * Date: 18.03.14
 * Time: 1:34
 */
public class Utils {

    public static String getRandomName() {
        String[] names = {"Aaron", "Abbey", "Acacia", "Adam", "Aden", "Adolph",
                "Alexia", "Alf", "Alexandria", "Amber", "Azura"};
        String[] surnames = {"Setters", "Shann", "Shaw", "Shield", "Settle",
                "Shady", "Share", "Shark", "Shill", "Sherwin"};
        Random rnd = new Random();
        return names[rnd.nextInt(names.length)] + " " + surnames[rnd.nextInt(surnames.length)];
    }

    public BufferedImage constructSprite(String path) {
        return null;
    }
}
