package hsport.com.example.king.takingtasks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

/**
 * Created by King on 07/11/2018.
 */

public class TasksHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";

    private static final int DATABASE_VERSION = 1;

    public TasksHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " ("
                + TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskEntry.COLUMN_TASK_TITLE + " TEXT NOT NULL, "
                + TaskEntry.COLUMN_TASK_DESC + " TEXT, "
                + TaskEntry.COLUMN_TASK_DATE + " INTEGER NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TASKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}