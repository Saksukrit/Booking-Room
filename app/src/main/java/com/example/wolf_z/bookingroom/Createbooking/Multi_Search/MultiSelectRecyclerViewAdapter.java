package com.example.wolf_z.bookingroom.Createbooking.Multi_Search;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Createbooking.ParticipantSearch;
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

        viewHolder.tvDisplayname.setText(accountBeens.get(position).getDisplayname());
        viewHolder.tvDepartment.setText(accountBeens.get(position).getDepartment());
        viewHolder.tvUsername.setText(accountBeens.get(position).getUsername());

        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return accountBeens.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView tvDisplayname;
        private TextView tvDepartment;
        private TextView tvUsername;
        private ClickListener listener;
        private final View selectedOverlay;


        public ViewHolder(View itemLayoutView, ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvDisplayname = (TextView) itemLayoutView.findViewById(R.id.tvName);
            tvDepartment = (TextView) itemLayoutView.findViewById(R.id.tvDepartment);
            tvUsername = (TextView) itemLayoutView.findViewById(R.id.tvUsername);
            selectedOverlay = (View) itemView.findViewById(R.id.selected_overlay);

            itemLayoutView.setOnClickListener(this);

            itemLayoutView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
//                Toast.makeText(v.getContext(), tvDisplayname.getText().toString(), Toast.LENGTH_SHORT).show();
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

