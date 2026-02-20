package com.arun.event.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arun.event.R;
import com.arun.event.db.model.Task;
import com.arun.event.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> list;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(
                LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(list != null) {
            Task task = list.get(position);
            holder.txtTitle.setText(task.getTitle());
            holder.txtStaffName.setText(task.getStaffName());
            holder.txtDept.setText(Utils.getDept().get(task.getDept()).getName());
            holder.txtSession.setText(Utils.getSession().get(task.getSession()).getName());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(task.getDate());
            holder.txtDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
            holder.txtMonth.setText(Utils.getMonths().get(calendar.get(Calendar.MONTH)));
            holder.txtYear.setText(calendar.get(Calendar.YEAR) + "");
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public Task getTaskAt(int position) {
        return list.get(position);
    }

    public void addTask(List<Task> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Task> getList() {
        return this.list;
    }
    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtStaffName, txtDept, txtSession, txtDay, txtMonth, txtYear;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtStaffName = itemView.findViewById(R.id.txtStaffName);
            txtDept = itemView.findViewById(R.id.txtDept);
            txtSession = itemView.findViewById(R.id.txtSession);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtYear = itemView.findViewById(R.id.txtYear);
        }
    }
}
