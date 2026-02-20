package com.arun.event.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.event.R;
import com.arun.event.repository.TaskRepository;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TASK_COUNT = 2;
    TextView txtDate;
    EditText edTitle, edDescription;
    Button btnAdd;
    String title, description;
    long date;
    int type = 1, id = 0;
    TaskRepository repository;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new TaskRepository(getApplication());

        Bundle bundle = getIntent().getExtras();
        date = bundle.getLong("date");
        type = bundle.getInt("type");
        if (type == 2) {
            id = bundle.getInt("id");
            title = bundle.getString("title");
            description = bundle.getString("description");
        }
        final Date dt = new Date(date);
        calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final int day = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

         int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);        // 12 hour clock
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        Log.d("DATE", "==============================Add Activity======================================");

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

        Log.d("DATE", "==============================Add Activity======================================");



        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(day + "-" + month + "-" + year);
        edTitle = findViewById(R.id.edTitle);
        edDescription = findViewById(R.id.edDescription);
        btnAdd = findViewById(R.id.btnAdd);

        if (type == 2) {
            if (title != null) edTitle.setText(title.trim());
            if (description != null) edDescription.setText(description.trim());
         }

        btnAdd.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnAdd) {
            if (type == 1) {
                repository.getAllTaskSpecCount(calendar.getTime()).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer >= TASK_COUNT) {
                            Toast.makeText(AddTaskActivity.this, "You Cant create more than 2 task per day", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            addTask();
                        }

                    }
                });
            } else addTask();
        }
    }

        private void addTask() {
            String title, description;

            title = edTitle.getText().toString().trim();
            description = edDescription.getText().toString().trim();

            Intent bundle = new Intent();
            bundle.putExtra("type", type);
            bundle.putExtra("id", id);
            bundle.putExtra("title", title);
            bundle.putExtra("description", description);
            bundle.putExtra("date", date);

            setResult(RESULT_OK, bundle);

            finish();
        }
}

