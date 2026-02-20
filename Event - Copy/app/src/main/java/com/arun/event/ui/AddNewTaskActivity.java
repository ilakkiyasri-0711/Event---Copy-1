package com.arun.event.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.arun.event.R;
import com.arun.event.adapter.DeptAdapter;
import com.arun.event.adapter.SessionAdapter;
import com.arun.event.db.model.Dept;
import com.arun.event.db.model.Session;
import com.arun.event.repository.TaskRepository;
import com.arun.event.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TASK_COUNT = 2;

    ImageButton btnDate;
    Spinner spDept, spSession;
    EditText edTitle, edMobile, edStaffName, edDate, edNotes;
    Button btnAdd;

    String mobileNumber, staffName, title, notes;
    long date;
    int type = 1, id = 0, dept = 1, session = 1;

    boolean isActivityClosed = false;

    TaskRepository repository;
    Calendar taskCalendar;

    DeptAdapter adDept;
    SessionAdapter adSession;
    List<Dept> listDept;
    List<Session> listSession;

    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupView();
        repository = new TaskRepository(getApplication());

        listDept = Utils.getDept();
        listSession = Utils.getSession();

        adDept = new DeptAdapter(this, R.layout.list_item, R.id.txtItem, listDept);
        spDept.setAdapter(adDept);
        adSession = new SessionAdapter(this, R.layout.list_item, R.id.txtItem, listSession);
        spSession.setAdapter(adSession);


        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        if (type == 2) {
            date = bundle.getLong("date");
            id = bundle.getInt("id");
            dept = bundle.getInt("dept");
            session = bundle.getInt("session");
            title = bundle.getString("title");
            mobileNumber = bundle.getString("mobileNumber");
            staffName = bundle.getString("staffName");
            notes = bundle.getString("notes");
            setValues();
        }

        spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setValues() {
        final Date dt = new Date(date);
        Calendar calendar = Calendar.getInstance();
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

        if(dayOfWeek==Calendar.SUNDAY){
            Toast.makeText(this,"You cannot select Sunday",Toast.LENGTH_SHORT).show();
            edDate.setText("");
        }else{
            edDate.setText(day + "-" + month + "-" + year);
        }


        spDept.setSelection(dept);
        spSession.setSelection(session);
        edTitle.setText(title);
        edMobile.setText(mobileNumber);
        edStaffName.setText(staffName);
        edNotes.setText(notes);
    }

    private void setupView() {
        edTitle = findViewById(R.id.edTitle);
        edMobile = findViewById(R.id.edMobile);
        edStaffName = findViewById(R.id.edStaffName);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        edDate = findViewById(R.id.edDate);
        edDate.setOnClickListener(this);
        btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
        spDept = findViewById(R.id.spDept);
        spSession = findViewById(R.id.spSession);
        edNotes = findViewById(R.id.edNotes);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnDate || viewId == R.id.edDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            taskCalendar = Calendar.getInstance();
                            taskCalendar.set(Calendar.HOUR, 0);
                            taskCalendar.set(Calendar.HOUR_OF_DAY, 0);
                            taskCalendar.set(Calendar.MINUTE, 0);
                            taskCalendar.set(Calendar.SECOND, 0);
                            taskCalendar.set(Calendar.MINUTE, 0);
                            taskCalendar.set(Calendar.MILLISECOND, 0);

                            taskCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            taskCalendar.set(Calendar.MONDAY, monthOfYear);
                            taskCalendar.set(Calendar.YEAR, year);

                            date = taskCalendar.getTimeInMillis();
                            Log.d("DATE", "date ---- \t\t: " + date);
                            int dayOfWeek = taskCalendar.get(Calendar.DAY_OF_WEEK);

                            if(dayOfWeek==Calendar.SUNDAY){
                                Toast.makeText(AddNewTaskActivity.this,"You cannot select Sunday.",Toast.LENGTH_SHORT).show();
                                edDate.setText("");
                            }else{
                                edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        } else if (viewId == R.id.btnAdd) {
            if (type == Utils.CREATE) {
                if (edTitle.getText().toString().length() != 0 &&
                        edDate.getText().toString().length() != 0 &&
                        edStaffName.getText().toString().length() != 0 &&
                        edMobile.getText().toString().length() != 0) {
                    repository.getAllTaskSpecCount(taskCalendar.getTime()).observe(this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if (integer >= TASK_COUNT) {
                                if (!isActivityClosed)
                                    Toast.makeText(AddNewTaskActivity.this, "You Cant create more than 2 task per day", Toast.LENGTH_SHORT).show();
                            } else {
                                repository.getSpecSessionCount(taskCalendar.getTime(), session).observe(AddNewTaskActivity.this, new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        final Integer count = integer;
                                        repository.getSpecSessionCount(taskCalendar.getTime(), 2).observe(AddNewTaskActivity.this, new Observer<Integer>() {
                                            @Override
                                            public void onChanged(Integer fullDayCount) {
                                                if(fullDayCount > 0) {
                                                    if (!isActivityClosed)
                                                        Toast.makeText(AddNewTaskActivity.this, "You Cant create event, session not available", Toast.LENGTH_SHORT).show();
                                                } else if (count > 0) {
                                                    String hour = session == 0 ? "Fore" : "After";
                                                    if (!isActivityClosed)
                                                        Toast.makeText(AddNewTaskActivity.this, "You Cant create more than 1 " + hour + " Noon" + " session per day", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (session == 2) {
                                                        repository.hasSession(taskCalendar.getTime()).observe(AddNewTaskActivity.this, new Observer<Integer>() {
                                                            @Override
                                                            public void onChanged(Integer hasSession) {
                                                                if (!isActivityClosed && hasSession > 0)
                                                                    Toast.makeText(AddNewTaskActivity.this, "You Cant create Full Day session", Toast.LENGTH_SHORT).show();
                                                                else addTask();
                                                            }
                                                        });
                                                    } else {
                                                        addTask();
                                                    }
                                                }
                                            }
                                        });

                                    }
                                });
                            }

                        }
                    });
                } else {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            } else if (type == Utils.UPDATE) {
                addTask();
            }
        }
    }

    private void addTask() {
        if (edTitle.getText().toString().length() != 0 &&
                edDate.getText().toString().length() != 0 &&
                edStaffName.getText().toString().length() != 0 &&
                edMobile.getText().toString().length() != 0) {
            String title, staffName, mobileNumber;

            title = edTitle.getText().toString().trim();
            staffName = edStaffName.getText().toString().trim();
            mobileNumber = edMobile.getText().toString().trim();
            notes = edNotes.getText().toString().trim();

            Intent bundle = new Intent();
            bundle.putExtra("type", type);
            bundle.putExtra("id", id);
            bundle.putExtra("dept", dept);
            bundle.putExtra("session", session);
            bundle.putExtra("title", title);
            bundle.putExtra("staffName", staffName);
            bundle.putExtra("notes", notes);
            bundle.putExtra("mobileNumber", mobileNumber);
            bundle.putExtra("date", date);

            setResult(RESULT_OK, bundle);

            isActivityClosed = true;
            finish();
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}