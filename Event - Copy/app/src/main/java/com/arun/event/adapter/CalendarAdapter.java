package com.arun.event.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arun.event.db.model.EventItem;
import com.arun.event.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    // for view inflation
    private LayoutInflater inflater;
    Calendar currentCalendar;
    List<EventItem> days = new ArrayList<>();
    Context context;
    public OnClickDay onClickDay;

    public interface OnClickDay {
       void OnItemLongClickDay(Date date, int position);
       void OnItemClickDay(Date date, int position);
    }

    public CalendarAdapter(Context context)
    {
        this.context = context;
        this.onClickDay = (OnClickDay) this.context;
    }

    public void update(List<EventItem> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(context);
            grid = inflater.inflate(R.layout.calendar_day, null);
        } else {
            grid = (View) convertView;
        }
        final Date date = days.get(position).getDate();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        final int day = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        LinearLayout linearDay = grid.findViewById(R.id.linearDay);
        TextView txtDay = grid.findViewById(R.id.txtDay);
        TextView txtEvent = grid.findViewById(R.id.txtEvent);
        txtDay.setText(day + "");
        txtEvent.setText(days.get(position).getCount() == 0 ? "0" : days.get(position).getCount() + "");

        if (days.get(position).getCount() != 0) {
            txtEvent.setVisibility(View.VISIBLE);
        }
        else {
            txtEvent.setVisibility(View.INVISIBLE);
        }

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        if (month != calendarToday.get(Calendar.MONTH) || year != calendarToday.get(Calendar.YEAR)) {
            // if this day is outside current month, grey it out
            txtDay.setTextColor(Color.parseColor("#716363"));
            //txtDay.setTypeface(txtDay.getTypeface(), Typeface.NORMAL);
        } else if (day == calendarToday.get(Calendar.DATE)) {
            // if it is today, set it to blue/bold
            txtDay.setTextColor(Color.RED);
            //txtDay.setBackgroundResource(R.drawable.round_textview);
        }
//        Iterator value = eventDays.iterator();

//        while (value.hasNext()) {
//            Date dt = (Date) value.next();
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(dt);
//            cal.add(Calendar.DAY_OF_MONTH, 1);
//
//            int d = cal.get(Calendar.DATE);
//            int m = cal.get(Calendar.MONTH);
//           // if (d == day && m == month) txtEvent.setVisibility(View.VISIBLE);
//            //else txtEvent.setVisibility(View.GONE);
//
//        }

            // set text
        //((TextView)view).setText(String.valueOf(calendar.get(Calendar.DATE)));


        linearDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDay.OnItemClickDay(date, position);
            }
        });

        linearDay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickDay.OnItemLongClickDay(date, position);
                return false;
            }
        });

        return grid;
    }
}