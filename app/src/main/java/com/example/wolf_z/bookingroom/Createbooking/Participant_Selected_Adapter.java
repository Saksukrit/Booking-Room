package com.example.wolf_z.bookingroom.Createbooking;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Participant_Selected_Adapter extends RecyclerView.Adapter<Participant_Selected_Adapter.TestViewHolder> {
    private ArrayList<AccountBean> accountBeen;
    private ArrayList<AccountBean> itemsPendingRemoval = new ArrayList<>();

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of accountBeen to pending runnables, so we can cancel a removal if need be

    public Participant_Selected_Adapter(ArrayList<AccountBean> accountBeen) {
        this.accountBeen = accountBeen;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, final int position) {
        final int positionUndo = position;
        final String displayname = accountBeen.get(position).getDisplayname();
        final String department = accountBeen.get(position).getDepartment();

        if (itemsPendingRemoval.contains(accountBeen.get(position))) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.tvdisplayname.setVisibility(View.VISIBLE); // show text
            holder.tvdisplayname.setText(displayname);
            holder.tvdepartment.setVisibility(View.GONE); // show text
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRangeChanged(position, getItemCount());
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(displayname);
                    pendingRunnables.remove(displayname);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(accountBeen.get(positionUndo));
                    // this will rebind the row in "normal" state
                    notifyItemChanged(accountBeen.indexOf(accountBeen.get(positionUndo)));
                }
            });

            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemRangeChanged(position, getItemCount());
                    remove(accountBeen.indexOf(accountBeen.get(positionUndo)));
                }
            });

        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.parseColor("#ffd477"));
            holder.tvdisplayname.setVisibility(View.VISIBLE);
            holder.tvdisplayname.setText(displayname);
            holder.tvdepartment.setVisibility(View.VISIBLE);
            holder.tvdepartment.setText(department);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);
            holder.deleteButton.setVisibility(View.GONE);
            holder.deleteButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return accountBeen.size();
    }

    /**
     * Utility method to add some rows for testing purposes. You can add rows from the toolbar menu.
     */

    void pendingRemoval(int position) {
        if (!itemsPendingRemoval.contains(accountBeen.get(position))) {
            itemsPendingRemoval.add(accountBeen.get(position));
            // this will redraw row in "undo" state
            notifyItemChanged(position);
        } else {
            notifyItemChanged(position);
        }
    }

    private void remove(int position) {
        if (itemsPendingRemoval.contains(accountBeen.get(position))) {
            itemsPendingRemoval.remove(accountBeen.get(position));
            // this will redraw row in "undo" state
            notifyItemChanged(position);
        }
        if (accountBeen.contains(accountBeen.get(position))) {
            accountBeen.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView tvdisplayname;
        TextView tvdepartment;
        Button undoButton;
        Button deleteButton;

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_name_selected, parent, false));
            tvdisplayname = (TextView) itemView.findViewById(R.id.tvdisplayname);
            tvdepartment = (TextView) itemView.findViewById(R.id.tvdepartment);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
        }

    }

}