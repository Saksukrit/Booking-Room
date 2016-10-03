package com.example.wolf_z.bookingroom.Createbooking.Multi_Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;

public class MultiSelectRecyclerViewAdapter extends SelectableAdapter<MultiSelectRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ViewHolder.ClickListener clickListener;
    private ArrayList<AccountBean> accountBeens;


    public MultiSelectRecyclerViewAdapter(Context context, ArrayList<AccountBean> accountBeens, ViewHolder.ClickListener clickListener) {
        this.accountBeens = accountBeens;
        this.mContext = context;
        this.clickListener = clickListener;

    }

    // Create new views
    @Override
    public MultiSelectRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_multiselect, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(accountBeens.get(position).getDisplayname());
        //username
        //department

        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return accountBeens.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView tvName;
        private ClickListener listener;
        private final View selectedOverlay;


        public ViewHolder(View itemLayoutView, ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
            selectedOverlay = (View) itemView.findViewById(R.id.selected_overlay);

            itemLayoutView.setOnClickListener(this);

            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
//                Toast.makeText(v.getContext(), tvName.getText().toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);

            public boolean onItemLongClicked(int position);
        }
    }


}

