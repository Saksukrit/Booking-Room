package com.example.wolf_z.bookingroom.Createbooking;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Createbooking.Delete_list_helper.DeleteRecyclerViewAdapter;
import com.example.wolf_z.bookingroom.Createbooking.Delete_list_helper.OnStartDragListener;
import com.example.wolf_z.bookingroom.Createbooking.Delete_list_helper.SimpleItemTouchHelperCallback;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;
import java.util.Objects;


public class ParticipantFragment extends Fragment implements OnStartDragListener {

    private final int REQ_CODE_participant_search = 12345;
    protected ProgressDialog prgDialog;
    protected Button search_participant_button;
    protected RecyclerView selected_recyclerview;
    private DeleteRecyclerViewAdapter item_selected_Adapter;
    private ItemTouchHelper mItemTouchHelper;
    protected Createbooking createbooking;

    public ParticipantFragment(Createbooking createbooking) {
        this.createbooking = createbooking;
    }

    public ParticipantFragment() {

    }

    public DeleteRecyclerViewAdapter getItem_selected_Adapter() {
        return item_selected_Adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_createbooking_participant, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        selected_recyclerview = (RecyclerView) view.findViewById(R.id.selected_list);

        if (!Objects.equals(createbooking.getFrom(), "detail_to_edit")) {
            setAdapter(createbooking.getAccountBeen_selected());
        }

        search_participant_button = (Button) view.findViewById(R.id.search_participant);
        search_participant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ParticipantSearchActivity.class);
                startActivityForResult(intent, REQ_CODE_participant_search);
            }
        });
        return view;
    }

    public void setAdapter(ArrayList<AccountBean> accountBeen_selected_arraylist) {
        item_selected_Adapter = new DeleteRecyclerViewAdapter(getActivity(), this, accountBeen_selected_arraylist);
        selected_recyclerview.setAdapter(item_selected_Adapter);
        selected_recyclerview.setHasFixedSize(true);
        selected_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(item_selected_Adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(selected_recyclerview);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
