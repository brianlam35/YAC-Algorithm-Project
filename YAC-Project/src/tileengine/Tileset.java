package tileengine;

import java.awt.*;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you", 0);
    public static final TETile GRASS = new TETile('#', new Color(126, 126, 126), Color.darkGray,
            "wall", 1);
    public static final TETile BORDER = new TETile('#', new Color(255, 255, 255), Color.darkGray,
            "wall", 1);
    public static final TETile END_ZONE = new TETile('#', new Color(204, 179, 114), Color.darkGray,
            "wall", 1);
    public static final TETile YARD_LINES = new TETile('#', new Color(255, 255, 255), Color.darkGray,
            "wall", 1);
    public static final TETile FLOOR = new TETile('·', Color.white, Color.black, "floor", 2);
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", 3);
    public static final TETile BRASS = new TETile('"', Color.green, Color.black, "grass", 4);
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water", 5);
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", 6);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", 7);
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", 8);
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", 9);
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain", 10);
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree", 11);

    public static final TETile CELL = new TETile('█', Color.white, Color.black, "cell", 12);


    public static final TETile NO_BALL_PLAYER = new TETile('@', new Color(195, 29, 29), Color.darkGray, "no ball player", 0);
    public static final TETile BALL_PLAYER = new TETile('@', new Color(128, 192, 128), Color.darkGray, "has ball player", 0);
    public static final TETile DEF_PLAYER = new TETile('@', new Color(23, 115, 220), Color.darkGray, "no ball player", 0);
}


