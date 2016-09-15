package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

public class CustomAdapter_subject extends BaseAdapter {

    Context mContext;
    String[] strName;

    public CustomAdapter_subject(Context context, String[] strName) {
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

        view = mInflater.inflate(R.layout.item_subjectlist, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.subject_item);
        textView.setText(strName[position]);

        return view;
    }
}
