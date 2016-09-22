package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

public class CustomAdapter_Pname extends BaseAdapter {

    Context mContext;
    String[] displayname;
    String[] department;

    public CustomAdapter_Pname(Context context, String[] displayname, String[] department) {
        this.mContext = context;
        this.displayname = displayname;
        this.department = department;
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

        view = mInflater.inflate(R.layout.item_namelist, parent, false);

        TextView displayname_item = (TextView) view.findViewById(R.id.displayname_item);
        displayname_item.setText(displayname[position]);

        TextView department_item = (TextView) view.findViewById(R.id.department_item);
        department_item.setText(department[position]);

        return view;
    }
}
