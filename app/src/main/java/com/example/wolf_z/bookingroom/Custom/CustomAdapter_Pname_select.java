package com.example.wolf_z.bookingroom.Custom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;

public class CustomAdapter_Pname_select extends BaseAdapter {

    ArrayList<AccountBean> accountBeens = new ArrayList<>();
    Context mContext;

    public CustomAdapter_Pname_select(Context context, ArrayList<AccountBean> accountBeens) {
        this.mContext = context;
        this.accountBeens = accountBeens;
    }

    @Override
    public int getCount() {
        return accountBeens.size();
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
        displayname_item.setText(accountBeens.get(position).getDisplayname());

        TextView department_item = (TextView) view.findViewById(R.id.department_item);
        department_item.setText(accountBeens.get(position).getDepartment());

        TextView username_item = (TextView) view.findViewById(R.id.username_item);
        username_item.setText(accountBeens.get(position).getUsername());

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("#ffd27f"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffedcc"));
        }
        return view;
    }
}
