package gangwal.org.todo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gangwal on 1/2/15.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASK = "task";
    public static final String INDEX = "idx";
    public static final String TITLE = "title";
    public static final String DUE_DATE = "dueDate";
    public static final String DUE_TIME = "dueTime";
    public static final String PRIORITY = "priority";
    public static final String DESCRIPTION = "description";

    public static final String DATABASE_NAME = "task.db";
    public static final int DATABASE_VERSION = 1;


    private static final String DATABASE_CREATE="CREATE TABLE " + TABLE_TASK + "("
            + INDEX + " INTEGER PRIMARY KEY, " + TITLE + " TEXT, "
            + DUE_DATE + " TEXT, "
            + DUE_TIME + " TEXT, "
            + PRIORITY + " TEXT, "
            + DESCRIPTION + " TEXT" + ")";
    public TaskDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
       database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TaskDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }
}