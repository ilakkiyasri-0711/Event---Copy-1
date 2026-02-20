package com.arun.event.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.arun.event.db.model.Task;

import java.util.Calendar;

@Database(entities = {Task.class} , version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao getTaskDao();

    private static volatile  TaskDatabase instance = null;

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TaskDatabase.class) {
                instance = create(context);
            }
        }
        return instance;
    }

    private static TaskDatabase create(Context context) {
        return Room.databaseBuilder(context.getApplicationContext()
                , TaskDatabase.class
                , "event")
               // .addCallback(callback)
                .build();
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("Database", "onCreate: call back");
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        public PopulateDbAsyncTask(TaskDatabase db) {
            this.taskDao = db.getTaskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("Database", "doInBackground: call back");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

            int hour = calendar.get(Calendar.HOUR);        // 12 hour clock
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int millisecond = calendar.get(Calendar.MILLISECOND);

            Log.d("DATE", "==============================Database======================================");

            Log.d("DATE", "year \t\t: " + year);
            Log.d("DATE", "month \t\t: " + month);
            Log.d("DATE", "dayOfMonth \t: " + dayOfMonth);
            Log.d("DATE", "dayOfWeek \t: " + dayOfWeek);
            Log.d("DATE", "weekOfYear \t: " + weekOfYear);
            Log.d("DATE", "weekOfMonth \t: " + weekOfMonth);

            Log.d("DATE", "hour \t\t: " + hour);
            Log.d("DATE", "hourOfDay \t: " + hourOfDay);
            Log.d("DATE", "minute \t\t: " + minute);
            Log.d("DATE", "second \t\t: " + second);
            Log.d("DATE", "millisecond \t\t: " + millisecond);

            Log.d("DATE", "==============================Database======================================");


         /*   taskDao.insert(new Task("Title 1", "Sh 1", calendar.getTime()));
            taskDao.insert(new Task("Title 2", "Sh 2", calendar.getTime()));
            taskDao.insert(new Task("Title 3", "Sh 3", calendar.getTime()));*/

            return null;
        }

    }

}
