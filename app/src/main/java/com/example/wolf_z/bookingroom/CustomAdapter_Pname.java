package com.example.wolf_z.bookingroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter_Pname extends BaseAdapter {

    Context mContext;
    String[] strName;

    public CustomAdapter_Pname(Context context, String[] strName) {
        this.mContext = context;
        this.strName = strName;
    }

    @Override
    public int getCount() {
        return strName.length;
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

        TextView textView = (TextView) view.findViewById(R.id.name_item);
        textView.setText(strName[position]);

        return view;
    }
}
