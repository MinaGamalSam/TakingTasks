package hsport.com.example.king.takingtasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import hsport.com.example.king.takingtasks.data.TasksContract.TaskEntry;

import hsport.com.example.king.takingtasks.data.TasksContract;

import static android.content.Context.MODE_PRIVATE;


public class CursorAdapter extends android.widget.CursorAdapter {
   public static CheckBox result;
    public static boolean checkBoxResult, resultsss ;


    public CursorAdapter(Context context, Cursor c) {

        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView main=view.findViewById(R.id.taskView);
        TextView subTitle=view.findViewById(R.id.dateView);

         result =view.findViewById(R.id.checkBox);
        checkBoxResult=result.isChecked();
        SharedPreferences.Editor editor = context.getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.putBoolean("checkBoxResult",checkBoxResult);
        editor.commit();

        SharedPreferences prefs =context.getSharedPreferences("prefs", MODE_PRIVATE);
        resultsss=prefs.getBoolean("checkBoxResult",checkBoxResult);

        if(resultsss) {


            int color=  ContextCompat.getColor(context, R.color.colorActive);
            view.setBackgroundColor(color);

        }else {
            int color=  ContextCompat.getColor(context, R.color.colorDeactive);
            view.setBackgroundColor(color);
        }



       String title= cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_TITLE));
       String date= cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DATE));

       if(TextUtils.isEmpty(title)){
           title="unkown";
       } if(TextUtils.isEmpty(date)){
           date="unkown";
       }

       main.setText(title);
       subTitle.setText("Start date is : "+date);

    }

}
