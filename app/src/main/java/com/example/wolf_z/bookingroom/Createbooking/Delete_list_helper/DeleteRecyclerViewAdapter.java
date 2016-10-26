package com.example.wolf_z.bookingroom.Createbooking.Delete_list_helper;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;
import java.util.Collections;


public class DeleteRecyclerViewAdapter extends RecyclerView.Adapter<DeleteRecyclerViewAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    private Context context;
    private final OnStartDragListener mDragStartListener;
    private ArrayList<AccountBean> accountBeens;

    public DeleteRecyclerViewAdapter(Context context, OnStartDragListener dragStartListener, ArrayList<AccountBean> accountBeens) {
        this.mDragStartListener = dragStartListener;
        this.accountBeens = accountBeens;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_multiselect, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.tvName.setText(accountBeens.get(position).getDisplayname());
        holder.tvDepartment.setText(accountBeens.get(position).getDepartment());
        holder.tvUsername.setText(accountBeens.get(position).getUsername());
        //3
    }

    @Override
    public void onItemDismiss(final int position) {
        int x = accountBeens.size();//0
        // confirm check;
//        Snackbar.make(, "you want delete ?", Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                accountBeens.remove(position);
                notifyItemRemoved(position);
//            }
//        }).show();

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(accountBeens, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return accountBeens.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        TextView tvName;
        TextView tvDepartment;
        TextView tvUsername;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDepartment = (TextView) itemView.findViewById(R.id.tvDepartment);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
