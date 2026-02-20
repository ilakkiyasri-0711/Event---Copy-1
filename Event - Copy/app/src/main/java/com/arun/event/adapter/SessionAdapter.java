package com.arun.event.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arun.event.R;
import com.arun.event.db.model.Session;

import java.util.List;

public class SessionAdapter extends ArrayAdapter<Session> {


    LayoutInflater flater;

    public SessionAdapter(Activity context, int resouceId, int textviewId, List<Session> list){
        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return  rowview(convertView,position);
    }

    private View rowview(View convertView, int position) {

        Session rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.list_item, null, false);

            holder.txtItem = (TextView) rowview.findViewById(R.id.txtItem);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtItem.setText(rowItem.getName());

        return rowview;
    }

    private class viewHolder {
        TextView txtItem;
    }
}