package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;

public class CustomAdapter_subject extends BaseAdapter {

    private Context mContext;
    private ArrayList<BookBean> bookBeens = new ArrayList<>();

    public CustomAdapter_subject(Context context, ArrayList<BookBean> bookBeens) {
        this.mContext = context;
        this.bookBeens = bookBeens;
    }

    @Override
    public int getCount() {
        return bookBeens.size();
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

        TextView subject_item = (TextView) view.findViewById(R.id.subject_name);
        subject_item.setText(bookBeens.get(position).getSubject());

        TextView subject_date = (TextView) view.findViewById(R.id.subject_date);
        subject_date.setText(bookBeens.get(position).getDate());

        TextView subject_room = (TextView) view.findViewById(R.id.subject_room);
        subject_room.setText(String.valueOf(bookBeens.get(position).getRoomid()));

        TextView subject_time = (TextView) view.findViewById(R.id.subject_time);
        subject_time.setText(bookBeens.get(position).getStarttime());

        TextView subject_bookingid = (TextView) view.findViewById(R.id.subject_bookingid);
        subject_bookingid.setText(String.valueOf(bookBeens.get(position).getBookingid()));     //String.valueOf() for set int

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("#ffd27f"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffedcc"));
        }
        return view;
    }
}
