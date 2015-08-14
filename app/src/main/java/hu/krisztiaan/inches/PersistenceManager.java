package hu.krisztiaan.inches;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersistenceManager {
    public static final String TAG = PersistenceManager.class.getName();
    static Context mContext;

    public static void init(Context c) {
        mContext = c;
    }

    public static List<Habit> getHabits() {
        return copyIterator(Habit.findAll(Habit.class));
    }

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public static void importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Log.i(TAG, "importDB sd.canWrite="+sd.canWrite());
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "hu.krisztiaan.inches"
                        + "//databases//" + "habits.db";
                String backupDBPath = "inches_backup"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i(TAG, "importDB load succesful");

            }
        } catch (Exception e) {

            Log.e(TAG, "importDB load failed with error: " + e.getMessage());

        }
    }

    public static void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            Log.i(TAG, "importDB sd.canWrite="+sd.canWrite());
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "hu.krisztiaan.inches"
                        + "//databases//" + "habits.db";
                String backupDBPath = "inches_backup";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i(TAG, "exportDB save succes");;

            }
        } catch (Exception e) {

            Log.e(TAG, "exportDB save failed with error: " +e.getMessage());

        }
    }
}
