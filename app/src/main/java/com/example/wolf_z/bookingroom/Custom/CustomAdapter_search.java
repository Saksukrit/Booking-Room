package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

public class CustomAdapter_search extends BaseAdapter {

    private Context mContext;
    private String[] Ssubject;
    private String[] Sdate;
    private String[] Sendtime;
    private int[] Sroomid;
    private String[] Sstarttime;

    public CustomAdapter_search(Context context, String[] Ssubject, String[] Sdate, String[] Sstarttime, String[] Sendtime, int[] Sroomid) {
        this.mContext = context;
        this.Ssubject = Ssubject;
        this.Sdate = Sdate;
        this.Sstarttime = Sstarttime;
        this.Sendtime = Sendtime;
        this.Sroomid = Sroomid;
    }

    @Override
    public int getCount() {
        return Ssubject.length;
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

        view = mInflater.inflate(R.layout.item_search, parent, false);

        TextView subject_item = (TextView) view.findViewById(R.id.subject_name);
        subject_item.setText(Ssubject[position]);

        TextView subject_date = (TextView) view.findViewById(R.id.subject_date);
        subject_date.setText(Sdate[position]);

        TextView subject_starttime = (TextView) view.findViewById(R.id.subject_starttime);
        subject_starttime.setText(Sstarttime[position]);

        TextView subject_endtime = (TextView) view.findViewById(R.id.subject_endtime);
        subject_endtime.setText(Sendtime[position]);

        TextView subject_roomid = (TextView) view.findViewById(R.id.subject_room);
        subject_roomid.setText(String.valueOf(Sroomid[position]));     //String.valueOf() for set int

        return view;
    }
}
