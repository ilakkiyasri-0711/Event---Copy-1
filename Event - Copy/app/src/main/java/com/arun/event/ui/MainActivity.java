package com.arun.event.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arun.event.adapter.CalendarAdapter;
import com.arun.event.db.model.EventItem;
import com.arun.event.R;
import com.arun.event.listeners.RecyclerTouchListener;
import com.arun.event.adapter.TaskAdapter;
import com.arun.event.db.model.Task;
import com.arun.event.listeners.ClickListener;
import com.arun.event.repository.TaskRepository;
import com.arun.event.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.itextpdf.text.pdf.PdfName.DEST;
import static com.itextpdf.text.pdf.PdfName.ca;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CalendarAdapter.OnClickDay {

    private static final int DAYS_COUNT = 42;
    private static final int TASK_COUNT = 2;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_LOGIN_CODE = 102;
    private static final int REQUEST_EDIT_CODE = 103;

    Date currentDate;
    GridView gridView;
    TextView txtMonthYear, txtCurrentDate;
    ImageView imgNextMonth, imgPrevMonth;
    Button btnGoToToday;
    FloatingActionButton fabAdd;
    Calendar currentCalendar, todayCalendar;
    RecyclerView recyclerView;
    List<String> monthList;
    TaskRepository repository;
    List<Task> dailyTaskList;
    ImageView btnPdf;
    private TaskViewModel viewModel;
    TaskAdapter dailyTaskAdapter;
    ArrayList<EventItem> calendarList, newCalendarList;
    int eventCount = 0;
    CalendarAdapter calendarAdapter;
    boolean isLogin = false;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD); // Set of font family alrady present with itextPdf library.
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarList = new ArrayList<>();
        newCalendarList = new ArrayList<>();
        dailyTaskList = new ArrayList<>();

        repository = new TaskRepository(getApplication());
        btnPdf = findViewById(R.id.btnPdf);
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }
        });
        addUIComponents();
        setUpCalendar();

//        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
//
//        viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
//            @Override
//            public void onChanged(List<Task> list) {
//                adapter.addTask(list);
//            }
//        });

        // TestViewModel viewModel1 = new ViewModelProvider(this).get(TestViewModel.class);

        loadList();

        setRecyclerSwipeListener(recyclerView);
        setRecyclerClickListener(recyclerView);
        //createPdf();
    }
    private  void addContent(Document document) throws DocumentException, DocumentException {

        // add a table
        createTable(document);

     }

    private  void createTable(Document document)
            throws DocumentException {
        PdfPTable table = new PdfPTable(5);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);


        PdfPCell c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Title"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Staff Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Dept"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Sesstion"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
        for (int i = 0; i < dailyTaskAdapter.getList().size(); i ++) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dailyTaskAdapter.getList().get(i).getDate());


            final int day = calendar.get(Calendar.DATE);
            final int mon = calendar.get(Calendar.MONTH);
            final int yr = calendar.get(Calendar.YEAR);
            String dateString = day + "-" + (mon + 1) + "-" + yr;

            table.addCell(dateString);
            table.addCell(dailyTaskAdapter.getList().get(i).getTitle());
            table.addCell(dailyTaskAdapter.getList().get(i).getStaffName());
            String dept = Utils.getDept().get(dailyTaskAdapter.getList().get(i).getDept()).getName();
            table.addCell(dept + "");
            String ses = Utils.getSession().get(dailyTaskAdapter.getList().get(i).getSession()).getName();
            table.addCell(ses + "");

        }


         document.add(table);
    }

    public void PrintDocument(String dest) throws IOException, java.io.IOException {
        try {

//            Document document = new Document();
            Document document = new Document(PageSize.A4.rotate());//PENGUIN_SMALL_PAPERBACK used to set the paper size
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            addContent(document);
            document.close();

            Toast.makeText(getApplicationContext(), "Report Generated", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createPdf() throws IOException {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/kasc/";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getString(R.string.folder_name) + File.separator;
        } else {
            root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.folder_name) + File.separator;
        }
        final File dir = new File(root);
        if (!dir.exists())
            dir.mkdirs();


        try {
            PrintDocument(dir.getPath() + File.separator +"semi-report.pdf");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    private void loadList() {
        repository.getAllDailyTask().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> list) {
                MainActivity.this.dailyTaskList = list;
                dailyTaskAdapter.addTask(MainActivity.this.dailyTaskList);
            }
        });
    }

    private void addUIComponents() {
        gridView = findViewById(R.id.gridView);
        txtMonthYear = findViewById(R.id.txtMonthYear);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        btnGoToToday = findViewById(R.id.btnGoToToday);
        imgNextMonth = findViewById(R.id.imgNextMonth);
        imgPrevMonth = findViewById(R.id.imgPrevMonth);
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(this);
        btnGoToToday.setOnClickListener(this);
        imgNextMonth.setOnClickListener(this);
        imgPrevMonth.setOnClickListener(this);

        calendarAdapter = new CalendarAdapter(this);
        gridView.setAdapter(calendarAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        dailyTaskAdapter = new TaskAdapter();
        recyclerView.setAdapter(dailyTaskAdapter);

    }

    private void setRecyclerClickListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(MainActivity.this.dailyTaskList.get(position).getDate());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                final int day = calendar.get(Calendar.DATE);
                final int mon = calendar.get(Calendar.MONTH);
                final int yr = calendar.get(Calendar.YEAR);
                String dateString = day + "-" + mon + "-" + yr;
              /*  Toast.makeText(MainActivity.this,
                        dateString+ "",
                        Toast.LENGTH_LONG).show();*/


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

                Log.d("DATE", "==============================Task Click======================================");

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

                Log.d("DATE", "==============================Task Click======================================");
/*                repository.getAllTaskSpecCount(calendar.getTime()).observe(MainActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        Toast.makeText(MainActivity.this, "" + integer, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @Override
            public void onLongClick(View view, int position) {

                if (isLogin) {
                    itemLoginClick(position);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_EDIT_CODE);
                }

            }
        }));
    }

    private void itemLoginClick(int position) {
        Date date = dailyTaskList.get(position).getDate();
        int id = dailyTaskList.get(position).getId();
        String title = dailyTaskList.get(position).getTitle();
        String staffName = dailyTaskList.get(position).getStaffName();
        String mobileNumber = dailyTaskList.get(position).getMobileNumber();
        String notes = dailyTaskList.get(position).getNotes();
        int dept = dailyTaskList.get(position).getDept();
        int session = dailyTaskList.get(position).getSession();

        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putLong("date", date.getTime());
        bundle.putInt("id", id);
        bundle.putInt("dept", dept);
        bundle.putInt("session", session);
        bundle.putString("title", title);
        bundle.putString("staffName", staffName);
        bundle.putString("notes", notes);
        bundle.putString("mobileNumber", mobileNumber);

        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setRecyclerSwipeListener(RecyclerView recyclerView) {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (isLogin) {
                    repository.delete(dailyTaskAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", viewHolder.getAdapterPosition());
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_LOGIN_CODE);
                }

            }
        }).attachToRecyclerView(recyclerView);

    }

    private void setUpCalendar() {

        monthList = new ArrayList<String>();
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("April");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("Jul");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");

        currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.HOUR, 0);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);

        todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR, 0);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        currentDate = new Date();
        updateCalendar();
    }

    public void updateCalendar() {
        txtMonthYear.setText(monthList.get(currentCalendar.get(Calendar.MONTH)) + " " + currentCalendar.get(Calendar.YEAR) + "");

        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int weekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);

        int hour = currentCalendar.get(Calendar.HOUR);        // 12 hour clock
        int hourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = currentCalendar.get(Calendar.MINUTE);
        int second = currentCalendar.get(Calendar.SECOND);
        int millisecond = currentCalendar.get(Calendar.MILLISECOND);

        Log.d("DATE", "==============================START======================================");

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

        Log.d("DATE", "==============================START======================================");


        Log.d("DATE", "updateCalendar: DAY_OF_WEEK " + currentCalendar.get(Calendar.DAY_OF_WEEK));
        // determine the cell for current month's beginning
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Log.d("DATE", "updateCalendar: DAY_OF_MONTH " + currentCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d("DATE", "updateCalendar: MONTH " + currentCalendar.get(Calendar.MONTH));
        Log.d("DATE", "updateCalendar: DAY_OF_WEEK " + currentCalendar.get(Calendar.DAY_OF_WEEK));
        Log.d("DATE", "updateCalendar:DAY_OF_WEEK " + currentCalendar.get(Calendar.DAY_OF_WEEK) + " === " + dayOfWeek);

        int monthBeginningCell = currentCalendar.get(Calendar.DAY_OF_WEEK) - 2;

        Log.d("DATE", "updateCalendar: CALCULATION " + currentCalendar.get(Calendar.DAY_OF_WEEK) + " - " + 2);
        Log.d("DATE", "updateCalendar: CALCULATION " + monthBeginningCell);

        // move calendar backwards to the beginning of the week
        Log.d("DATE", "updateCalendar: 1 DAY_OF_MONTH " + currentCalendar.get(Calendar.DAY_OF_MONTH));
        if (monthBeginningCell == -1) monthBeginningCell = 6;
        currentCalendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        Log.d("DATE", "updateCalendar: 2 DAY_OF_MONTH " + currentCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d("DATE", "updateCalendar: 2 MONTH " + currentCalendar.get(Calendar.MONTH));


        // fill cells
        calendarList.clear();
        while (calendarList.size() < DAYS_COUNT) {

            final int eventCount = 0;//repository.getAllTaskSpecCount(currentCalendar.getTime());

            EventItem eventItem = new EventItem(eventCount, currentCalendar.getTime());
            calendarList.add(eventItem);

            currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
            // oldMonth = currentCalendar.get(Calendar.MONTH);
        }

        calendarAdapter.update(calendarList);

        txtCurrentDate.setText(todayCalendar.get(Calendar.DAY_OF_MONTH) + " - "
                + (todayCalendar.get(Calendar.MONTH) + 1) + " - "
                + todayCalendar.get(Calendar.YEAR));


        for (int i = 0; i < calendarList.size(); i++) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarList.get(i).getDate());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            final int finalI = i;
//            Log.d("DATE", "onChanged loop count: -------------> " + finalI);

            repository.getAllTaskSpecCount(calendar.getTime()).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    //                  Log.d("DATE", "onChanged event count: -------------> " + integer.toString());
                    //                Log.d("DATE", "onChanged event count list: -------------> " + calendarList.get(finalI).getCount());
                    calendarList.get(finalI).setCount(integer);
                    calendarAdapter.update(calendarList);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imgPrevMonth) {

            int year = currentCalendar.get(Calendar.YEAR);
            int month = currentCalendar.get(Calendar.MONTH); // Jan = 0, dec = 11
            int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
            int weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR);
            int weekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);

            int hour = currentCalendar.get(Calendar.HOUR);        // 12 hour clock
            int hourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            int minute = currentCalendar.get(Calendar.MINUTE);
            int second = currentCalendar.get(Calendar.SECOND);
            int millisecond = currentCalendar.get(Calendar.MILLISECOND);

            Log.d("DATE", "==============================BEFORE======================================");

            Log.d("DATE", "year \t\t: " + year);
            Log.d("DATE", "month \t\t: " + month);
            Log.d("DATE", "dayOfMonth \t: " + dayOfMonth);
            Log.d("DATE", "dayOfWeek \t: " + dayOfWeek);
            Log.d("DATE", "weekOfYear \t: " + weekOfYear);
            Log.d("DATE", "weekOfMonth \t: " + weekOfMonth);

            Log.d("DATE", "hour \t\t: " + hour);
            Log.d("DATE", "hourOfDay \t: " + hourOfDay);
            Log.d("DATE", "minute \t\t: " + minute);

            Log.d("DATE", "==============================BEFORE======================================");

            currentCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) - 2);
            Log.d("MONTH", "onClick: " + currentCalendar.get(Calendar.MONTH));


            year = currentCalendar.get(Calendar.YEAR);
            month = currentCalendar.get(Calendar.MONTH); // Jan = 0, dec = 11
            dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
            dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR);
            weekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);

            hour = currentCalendar.get(Calendar.HOUR);        // 12 hour clock
            hourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            minute = currentCalendar.get(Calendar.MINUTE);
            second = currentCalendar.get(Calendar.SECOND);
            millisecond = currentCalendar.get(Calendar.MILLISECOND);

            Log.d("DATE", "==============================AFTER======================================");

            Log.d("DATE", "year \t\t: " + year);
            Log.d("DATE", "month \t\t: " + month);
            Log.d("DATE", "dayOfMonth \t: " + dayOfMonth);
            Log.d("DATE", "dayOfWeek \t: " + dayOfWeek);
            Log.d("DATE", "weekOfYear \t: " + weekOfYear);
            Log.d("DATE", "weekOfMonth \t: " + weekOfMonth);

            Log.d("DATE", "hour \t\t: " + hour);
            Log.d("DATE", "hourOfDay \t: " + hourOfDay);
            Log.d("DATE", "minute \t\t: " + minute);

            Log.d("DATE", "==============================AFTER======================================");

            updateCalendar();
        } else if (v.getId() == R.id.imgNextMonth) {

            int year = currentCalendar.get(Calendar.YEAR);
            int month = currentCalendar.get(Calendar.MONTH); // Jan = 0, dec = 11
            int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
            int weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR);
            int weekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);

            int hour = currentCalendar.get(Calendar.HOUR);        // 12 hour clock
            int hourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            int minute = currentCalendar.get(Calendar.MINUTE);
            int second = currentCalendar.get(Calendar.SECOND);
            int millisecond = currentCalendar.get(Calendar.MILLISECOND);

            Log.d("DATE", "==============================BEFORE======================================");

            Log.d("DATE", "year \t\t: " + year);
            Log.d("DATE", "month \t\t: " + month);
            Log.d("DATE", "dayOfMonth \t: " + dayOfMonth);
            Log.d("DATE", "dayOfWeek \t: " + dayOfWeek);
            Log.d("DATE", "weekOfYear \t: " + weekOfYear);
            Log.d("DATE", "weekOfMonth \t: " + weekOfMonth);

            Log.d("DATE", "hour \t\t: " + hour);
            Log.d("DATE", "hourOfDay \t: " + hourOfDay);
            Log.d("DATE", "minute \t\t: " + minute);

            Log.d("DATE", "==============================BEFORE======================================");

            // currentCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) + 1);
            Log.d("MONTH", "onClick: " + currentCalendar.get(Calendar.MONTH));

            // currentCalendar.add(Calendar.MONTH,1);
//            Log.d("MONTH", "onClick: " + currentCalendar.get(Calendar.MONTH));


            year = currentCalendar.get(Calendar.YEAR);
            month = currentCalendar.get(Calendar.MONTH); // Jan = 0, dec = 11
            dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
            dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR);
            weekOfMonth = currentCalendar.get(Calendar.WEEK_OF_MONTH);

            hour = currentCalendar.get(Calendar.HOUR);        // 12 hour clock
            hourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
            minute = currentCalendar.get(Calendar.MINUTE);
            second = currentCalendar.get(Calendar.SECOND);
            millisecond = currentCalendar.get(Calendar.MILLISECOND);

            Log.d("DATE", "==============================AFTER======================================");

            Log.d("DATE", "year \t\t: " + year);
            Log.d("DATE", "month \t\t: " + month);
            Log.d("DATE", "dayOfMonth \t: " + dayOfMonth);
            Log.d("DATE", "dayOfWeek \t: " + dayOfWeek);
            Log.d("DATE", "weekOfYear \t: " + weekOfYear);
            Log.d("DATE", "weekOfMonth \t: " + weekOfMonth);

            Log.d("DATE", "hour \t\t: " + hour);
            Log.d("DATE", "hourOfDay \t: " + hourOfDay);
            Log.d("DATE", "minute \t\t: " + minute);

            Log.d("DATE", "==============================AFTER======================================");

            updateCalendar();
        } else if (v.getId() == R.id.btnGoToToday) {
            currentCalendar = todayCalendar;
            updateCalendar();
        } else if (v.getId() == R.id.fabAdd) {
            createNewTask();
        }

    }

    private void createNewTask() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final int day = calendar.get(Calendar.DATE);
        final int mon = calendar.get(Calendar.MONTH);
        final int yr = calendar.get(Calendar.YEAR);

        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);

        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);

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

        Log.d("DATE", "==============================Create New Task======================================");

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

        Log.d("DATE", "==============================Create New Task======================================");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            long date = data.getLongExtra("date", 0);
            String title = data.getStringExtra("title");
            String staffName = data.getStringExtra("staffName");
            String mobileNumber = data.getStringExtra("mobileNumber");
            String notes = data.getStringExtra("notes");
            int dept = data.getIntExtra("dept", 1);
            int session = data.getIntExtra("session", 1);
            int type = data.getIntExtra("type", 1);
            if (type == Utils.CREATE) create(title, dept, session, staffName, mobileNumber, notes, new Date(date));
            else if (type == Utils.UPDATE) {
                int id = data.getIntExtra("id", -1);
                update(id, title, dept, session, staffName, mobileNumber, notes, new Date(date));
            }
            loadList();
        } else if (requestCode == REQUEST_LOGIN_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            Boolean login = data.getBooleanExtra("login", false);
            isLogin = login;
            if (isLogin)
                repository.delete(dailyTaskAdapter.getTaskAt(position));
            loadList();
        } else if (requestCode == REQUEST_EDIT_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            Boolean login = data.getBooleanExtra("login", false);
            isLogin = login;
            if (isLogin) itemLoginClick(position);
            loadList();
        } else loadList();
    }

    private void create(String title, int dept, int session, String staffName, String mobileNumber, String notes, Date date) {
        Task task = new Task(title, dept, session, staffName, mobileNumber, notes, date);
        repository.insert(task);
    }

    private void update(int id, String title, int dept, int session, String staffName, String mobileNumber, String notes, Date date) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDept(dept);
        task.setSession(session);
        task.setStaffName(staffName);
        task.setMobileNumber(mobileNumber);
        task.setDate(date);
        task.setNotes(notes);
        repository.update(task);
    }

    @Override
    public void OnItemLongClickDay(final Date date, int position) {
        // dateLongClick(date, position);
    }

    private void dateLongClick(Date date, int position) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final int day = calendar.get(Calendar.DATE);
        final int mon = calendar.get(Calendar.MONTH);
        final int yr = calendar.get(Calendar.YEAR);

        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putLong("date", date.getTime());

        Intent intent = new Intent(MainActivity.this, AddNewTaskActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);

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

        Log.d("DATE", "==============================Item Long Click======================================");

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

        Log.d("DATE", "==============================Item Long Click======================================");

    }

    @Override
    public void OnItemClickDay(final Date date, int position) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final int day = calendar.get(Calendar.DATE);
        final int mon = calendar.get(Calendar.MONTH);
        final int yr = calendar.get(Calendar.YEAR);
        String dateString = day + "-" + mon + "-" + yr;
        //Toast.makeText(this, "Click : " + dateString, Toast.LENGTH_SHORT).show();
        repository.getAllTaskSpec(calendar.getTime()).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> list) {
                MainActivity.this.dailyTaskList = list;
                dailyTaskAdapter.addTask(MainActivity.this.dailyTaskList);
            }
        });
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

        Log.d("DATE", "==============================Item Click======================================");

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

        Log.d("DATE", "==============================Item Click======================================");


    }

    private void requestStoragePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if(dailyTaskAdapter.getList().size() != 0) {
                                try {
                                    //createFile();
                                    createPdf();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "List empty please create event", Toast.LENGTH_LONG).show();
                            }

                          //  Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void createFile() {
        try {
            String rootPath = "";//Environment.getExternalStorageDirectory().getAbsolutePath() + "/arunnewfolder/";    // it will return root directory of internal storage
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + getString(R.string.folder_name) + File.separator;
            } else {
                rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.folder_name) + "/";
            }
            File root = new File(rootPath);
            if (!root.exists()) {
                Boolean bool = root.mkdirs();       // create folder if not exist
                System.out.println("my dir log: " + bool);
            }
            File file = new File(rootPath + "log.txt");
            if (!file.exists()) {
                file.createNewFile();   // create file if not exist
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append("hi this will write in to file");
            buf.newLine();  // pointer will be nextline
            buf.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
