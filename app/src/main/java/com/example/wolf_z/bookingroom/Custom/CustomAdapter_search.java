package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;

public class CustomAdapter_search extends BaseAdapter {

    private Context mContext;
    private ArrayList<BookBean> bookBean_search = new ArrayList<>();

    public CustomAdapter_search(Context context, ArrayList<BookBean> bookBean) {
        this.mContext = context;
        this.bookBean_search = bookBean;
    }

    @Override
    public int getCount() {
        return bookBean_search.size();
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
        subject_item.setText(bookBean_search.get(position).getSubject());

        TextView subject_date = (TextView) view.findViewById(R.id.subject_date);
        subject_date.setText(bookBean_search.get(position).getDate());

        TextView subject_starttime = (TextView) view.findViewById(R.id.subject_starttime);
        subject_starttime.setText(bookBean_search.get(position).getStarttime());

        TextView subject_endtime = (TextView) view.findViewById(R.id.subject_endtime);
        subject_endtime.setText(bookBean_search.get(position).getEndtime());

        TextView subject_roomid = (TextView) view.findViewById(R.id.subject_room);
        subject_roomid.setText(String.valueOf(bookBean_search.get(position).getRoomid()));     //String.valueOf() for set int

        return view;
    }
}
