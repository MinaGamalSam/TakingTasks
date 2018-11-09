package hsport.com.example.king.takingtasks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

/**
 * Created by King on 07/11/2018.
 */

public class TasksProvider extends ContentProvider {

    public static final String LOG_TAG= TasksProvider.class.getSimpleName();

    private static final int Tasks = 100;

    /** URI matcher code for the content URI for a single pet in the Tasks table */
    private static final int Tasks_ID = 101;

    
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    
    static {
        
        sUriMatcher.addURI(TasksContract.CONTENT_AUTHORITY, TasksContract.PATH_TASKS, Tasks);
        
        sUriMatcher.addURI(TasksContract.CONTENT_AUTHORITY, TasksContract.PATH_TASKS + "/#", Tasks_ID);
    }

    /** Database helper object */
    private TasksHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper=new TasksHelper(getContext());
        return false;
    }

    
    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database=mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case Tasks:
                
                cursor = database.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case Tasks_ID:
                
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        
        return cursor;
    }

    
    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Tasks:
                return TaskEntry.CONTENT_LIST_TYPE;
            case Tasks_ID:
                return TaskEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

   
    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Tasks:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        // Check that the name is not null
     /*   String name = values.getAsString(TaskEntry.COLUMN_TASK_TITLE);
        if (name == null) {
            throw new IllegalArgumentException("Task requires a title");
        }

        // Check that the gender is valid
        String description = values.getAsString(TaskEntry.COLUMN_TASK_DESC);
        if (description == null ) {
            throw new IllegalArgumentException("Pet requires valid description");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer date = values.getAsInteger(TaskEntry.COLUMN_TASK_DATE);
        if (date == null ) {
            throw new IllegalArgumentException("Task requires valid date");
        }*/

        // No need to check the breed, any value is valid (including null).

        String name = values.getAsString(TaskEntry.COLUMN_TASK_TITLE);
        String description = values.getAsString(TaskEntry.COLUMN_TASK_DESC);
        String date = values.getAsString(TaskEntry.COLUMN_TASK_DATE);
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(TaskEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Tasks:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case Tasks_ID:
                // Delete a single row given by the ID in the URI
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }


    @Override
    public int update( Uri uri,  ContentValues contentValues,  String selection,  String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Tasks:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case Tasks_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(TaskEntry.COLUMN_TASK_TITLE)) {
            String name = values.getAsString(TaskEntry.COLUMN_TASK_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(TaskEntry.COLUMN_TASK_DATE)) {
            Integer date = values.getAsInteger(TaskEntry.COLUMN_TASK_DATE);
            if (date != null && date < 0 ) {
                throw new IllegalArgumentException("Task requires valid date");
            }
        }

        if (values.containsKey(TaskEntry.COLUMN_TASK_DESC)) {
            // Check that the weight is greater than or equal to 0 kg
            String description = values.getAsString(TaskEntry.COLUMN_TASK_DESC);
            if (description == null ) {
                throw new IllegalArgumentException("Pet requires description");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
