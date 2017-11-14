package com.iogb.sheduletown;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IOGB Irvin Omar Galindo Becerra on 08/11/2017.
 */

public class AdapterItem extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<TeacherList> items;

    public AdapterItem(Activity activity, ArrayList<TeacherList> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear()
    {
        items.clear();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addAll(ArrayList<TeacherList> list) {
        for (int i = 0 ; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view==null)
        {
            LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.activity_list_view,null);
        }

        TeacherList teacherList=items.get(position);

        TextView classroom=(TextView) view.findViewById(R.id.tv_classroom);
        classroom.setText(teacherList.getClassroom());
        TextView subject =(TextView) view.findViewById(R.id.tv_subject);
        subject.setText(teacherList.getSubject());
        TextView hour =(TextView) view.findViewById(R.id.tv_hour);
        hour.setText(teacherList.getHour());

        return view;
    }
}
