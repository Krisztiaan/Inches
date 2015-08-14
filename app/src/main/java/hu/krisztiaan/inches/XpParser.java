package hu.krisztiaan.inches;

/**
 * Created by Krisz on 2015.07.21..
 */
public class XpParser {
    public static final int XP_PER_LEVEL = 50;

    public static int getLevel(int xp) {
        return (xp/XP_PER_LEVEL)+1;
    }

    public static int getLevelProgress(int xp) {
        return (xp%XP_PER_LEVEL);
    }
}
