package hu.krisztiaan.inches;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Krisz on 2015.07.21..
 */
public class Habit extends SugarRecord<Habit> {
    public static final int XP_DAILY = 10;
    public static final int XP_HOURLY = 3;

    public String text;
    public int xp = 0;
    public Date lastProgress = new Date(0);
    public Date lastLevelUp = new Date(0);

    public Habit() {

    }

    public boolean isChecked() {
        Calendar c1 = Calendar.getInstance(); // ma

        Calendar c2 = Calendar.getInstance();
        c2.setTime(lastProgress); // utolsó szerkesztés

        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public boolean isLeveledUp() {
        Calendar c1 = Calendar.getInstance();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(lastLevelUp); // utolsó szintlépés

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public void progress() {
        if (lastProgress != null) {
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DAY_OF_YEAR, -1); // tegnap

            Calendar c2 = Calendar.getInstance();
            c2.setTime(lastProgress); // utolsó szerkesztés

            Calendar c3 = Calendar.getInstance();
            c3.add(Calendar.HOUR_OF_DAY, -1);

            if (c3.compareTo(c2) == 1) {
                if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                        && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                    addXp(XP_DAILY);
                }
            }
        }
        addXp(XP_HOURLY);
        lastProgress = new Date();
        save();
    }

    public void addXp(int xpAdd) {
        Calendar c1 = Calendar.getInstance(); // ma
        c1.add(Calendar.HOUR, -1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(lastProgress); // utolsó szerkesztés

        if (c1.compareTo(c2) == 1) {
            int lastLevel = XpParser.getLevel(xp);
            xp += xpAdd;
            if (XpParser.getLevel(xp) > lastLevel) lastLevelUp = new Date();
        }
    }
}
