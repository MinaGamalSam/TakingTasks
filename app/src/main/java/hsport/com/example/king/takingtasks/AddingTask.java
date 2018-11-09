package hsport.com.example.king.takingtasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

public class AddingTask extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ADDING_LOADER = 2;
    private EditText title, description;
    private TextView date;
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    private Button seteDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_task);
        title=findViewById(R.id.editText);
        date=findViewById(R.id.editText2);
        description=findViewById(R.id.editText3);
        seteDate=findViewById(R.id.datePicker);

        seteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                datePickerDialog=new DatePickerDialog(AddingTask.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month=month+1;
                        date.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },day,month,year);
                datePickerDialog.show();
            }
        });


        getLoaderManager().initLoader(ADDING_LOADER,null,this);
    }

private void savetask(){
        String titles=title.getText().toString();
        String descriptions=description.getText().toString();
        String dates=date.getText().toString();
        if(TextUtils.isEmpty(titles) ||TextUtils.isEmpty(descriptions)  ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("InValid input");
            builder.setPositiveButton("ok", null);


            builder.show();
        }else{
            ContentValues values= new ContentValues();
            values.put(TaskEntry.COLUMN_TASK_TITLE,titles);
            values.put(TaskEntry.COLUMN_TASK_DESC,descriptions);
            values.put(TaskEntry.COLUMN_TASK_DATE,dates);
            Uri newUri = getContentResolver().insert(TaskEntry.CONTENT_URI, values);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Adding task is failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "successful adding task",
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }

}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TaskEntry._ID,
                TaskEntry.COLUMN_TASK_TITLE,
                TaskEntry.COLUMN_TASK_DESC,
                TaskEntry.COLUMN_TASK_DATE,
        };
        return new CursorLoader(this,
                TaskEntry.CONTENT_URI,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    title.setText("");
    date.setText("");
    description.setText("");
    }

    public void saveData(View view) {
        savetask();

    }
}
