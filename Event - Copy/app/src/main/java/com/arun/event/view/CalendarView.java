package com.arun.event.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arun.event.R;

import java.util.Date;

public class CalendarView extends LinearLayout {
    // calendar components
    LinearLayout header;
    Button btnToday;
    ImageView btnPrev;
    ImageView btnNext;
    TextView txtDateDay;
    TextView txtDisplayDate;
    TextView txtDateYear;
    GridView gridView;
    Date currentDate = new Date();
    private int DAYS_COUNT = 7;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        btnPrev = findViewById(R.id.imgPrevMonth);
        btnNext = findViewById(R.id.imgNextMonth);
        txtDateYear = findViewById(R.id.txtCurrentDate);
        txtDisplayDate = findViewById(R.id.txtMonthYear);
        btnToday = findViewById(R.id.btnGoToToday);
        gridView = findViewById(R.id.gridView);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);
        assignUiElements();
    }


}
