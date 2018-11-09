package hsport.com.example.king.takingtasks;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

public class TaskDetailss extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAILS_LOADER = 1;
    private TextView title, description, date;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_details);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        date=findViewById(R.id.date);

        Intent intent =getIntent();
        currentUri=intent.getData();

        getLoaderManager().initLoader(DETAILS_LOADER,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TaskEntry._ID,
                TaskEntry.COLUMN_TASK_TITLE,
                TaskEntry.COLUMN_TASK_DESC,
                TaskEntry.COLUMN_TASK_DATE,
        };
        return new CursorLoader(this,
                currentUri,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor==null || cursor.getCount()<0){
            return;
        }
         if(cursor.moveToFirst()){
            String titles =cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_TITLE));
            String desc =cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DESC));
            String dates =cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DATE));

             if(TextUtils.isEmpty(titles)){
                 titles="unkown";
             } if(TextUtils.isEmpty(dates)){
                 dates="unkown";
             }if(TextUtils.isEmpty(desc)){
                 desc="unkown";
             }
            title.setText(titles);
            description.setText(desc);
            date.setText(dates);
         }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        title.setText("");
        description.setText("");
        date.setText("");

    }

    public void addTask(View view) {
        startActivity(new Intent(TaskDetailss.this,AddingTask.class));
    }

    public void deleteTaskk(View view) {
        int deletedRows=getContentResolver().delete(currentUri,null,null);
        if(deletedRows!=0){
            Toast.makeText(this, "Delete this task", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "Cann't Delete this task", Toast.LENGTH_SHORT).show();
        }
    }
}
