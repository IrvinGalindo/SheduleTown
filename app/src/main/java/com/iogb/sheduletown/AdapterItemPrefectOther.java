package com.iogb.sheduletown;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by L55-C5211R on 15/11/2017.
 */
public class AdapterItemPrefectOther extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<PrefectList> items;
    protected PrefectList prefectList;
    protected int nvaPos=0;

    public AdapterItemPrefectOther(Activity activity, ArrayList<PrefectList> items) {
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
    public PrefectList getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addAll(ArrayList<PrefectList> list) {
        for (int i = 0 ; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view =convertView ;

        if (convertView==null)
        {

            LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.prefect_view_other,null);
        }

        prefectList=items.get(position);

        TextView teacherName=(TextView) view.findViewById(R.id.tv_teacher_name);
        teacherName.setText(prefectList.getNameTeacher());
        ImageView photo =(ImageView) view.findViewById(R.id.iv_foto);
        Bitmap bm = prefectList.getPhoto();
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(Resources.getSystem(), bm);
        roundedDrawable.setCornerRadius(bm.getHeight());

        photo.setImageDrawable(roundedDrawable);
        TextView classrom=(TextView)view.findViewById(R.id.tv_classrooms);
        classrom.setText(prefectList.getClassroom());
        CheckBox checkBox =(CheckBox)view.findViewById(R.id.cb_check);
        if(prefectList.isAssistents()==1){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
        final View finalView = view;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    items.get(position).setAssistents(1);
                }
            }
        });

        return view;
    }
}
