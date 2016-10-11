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

public class CustomAdapter_Pname extends BaseAdapter {

    Context mContext;
    ArrayList<AccountBean> accountBean = new ArrayList<>();

    public CustomAdapter_Pname(Context context, ArrayList<AccountBean> accountBean) {
        this.mContext = context;
        this.accountBean = accountBean;
    }

    @Override
    public int getCount() {
        return accountBean.size();
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
        displayname_item.setText(accountBean.get(position).getDisplayname());

        TextView department_item = (TextView) view.findViewById(R.id.department_item);
        department_item.setText(accountBean.get(position).getDepartment());

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("#ffd27f"));
        } else {
            view.setBackgroundColor(Color.parseColor("#ffedcc"));
        }
        return view;
    }
}
