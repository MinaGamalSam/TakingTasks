package hsport.com.example.king.takingtasks;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASKS_LOADER = 1;
    CursorAdapter cursorAdapter;
    int deletedRows;
    //private   CheckBox result;
    private ListView taskListView;
    Uri currentPetUri, chechBoxUri;
    /*public static boolean res;
    public static boolean checkBoxResults;
*/

/*    @Override
    protected void onStart() {
        super.onStart();
SharedPreferences prefs =getActivity.getSharedPreferences("prefs", MODE_PRIVATE);
res =prefs.getBoolean("checkBoxResult",CursorAdapter.checkBoxResult);

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddingTask.class);
                startActivity(intent);
            }
        });
        // result=findViewById(R.id.checkBox);


        taskListView = (ListView) findViewById(R.id.list_coz);


        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        taskListView.setEmptyView(emptyView);

        cursorAdapter = new CursorAdapter(this, null);
        taskListView.setAdapter(cursorAdapter);

        // Setup the item click listener
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, TaskDetailss.class);

                currentPetUri = ContentUris.withAppendedId(TaskEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);


                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(TASKS_LOADER, null, this);
    }

    private void deleteALlTasks() {
        deletedRows = getContentResolver().delete(TaskEntry.CONTENT_URI, null, null);

    }
  /*  private void updateColor(){

        CursorAdapter.result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechBoxUri=ContentUris.withAppendedId(TaskEntry.CONTENT_URI,v.getId());
                getContentResolver().notifyChange(chechBoxUri, null);
            }
        });

    }*/

    private void insertTask() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_TASK_TITLE, "Java Activity");
        values.put(TaskEntry.COLUMN_TASK_DATE, "20/1/2019");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.

        Uri newUri = getContentResolver().insert(TaskEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getLayoutInflater().inflate(R.menu.main_menu,menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_ids:
                deleteALlTasks();
                if (deletedRows != 0) {
                    Toast.makeText(this, "All tasks deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There is no tasks", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.exit:
                //finish();
                insertTask();

                return true;
        }
        return true;
    }

    /*public View getView (int position, View convertView, ViewGroup parent){
        if( convertView == null ){
            //We must create a View:
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        //Here we can do changes to the convertView, such as set a text on a TextView
        //or an image on an ImageView.
        return convertView;
    }*/


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TaskEntry._ID,
                TaskEntry.COLUMN_TASK_TITLE,
                TaskEntry.COLUMN_TASK_DESC,
                TaskEntry.COLUMN_TASK_DATE,
        };
        return new CursorLoader(this,
                TaskEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cursorAdapter.swapCursor(data);

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void upDateCheck(View view) {
        chechBoxUri = ContentUris.withAppendedId(TaskEntry.CONTENT_URI, view.getId());
        getContentResolver().notifyChange(chechBoxUri, null);

    }
}
