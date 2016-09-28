package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

public class CustomAdapter_Pname_select extends BaseAdapter {

    Context mContext;
    String[] displayname;
    String[] department;
    String[] username;

    public CustomAdapter_Pname_select(Context context, String[] displayname, String[] department, String[] username) {
        this.mContext = context;
        this.displayname = displayname;
        this.department = department;
        this.username = username;
    }

    @Override
    public int getCount() {
        return displayname.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = mInflater.inflate(R.layout.item_namelist_select, parent, false);

        TextView displayname_item = (TextView) view.findViewById(R.id.displayname_item);
        displayname_item.setText(displayname[position]);

        TextView department_item = (TextView) view.findViewById(R.id.department_item);
        department_item.setText(department[position]);

        TextView username_item = (TextView) view.findViewById(R.id.username_item);
        username_item.setText(username[position]);

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("#ffd27f"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffedcc"));
        }
        return view;
    }
}
