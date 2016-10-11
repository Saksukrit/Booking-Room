package com.example.wolf_z.bookingroom.Createbooking;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wolf_z.bookingroom.Createbooking.Multi_Search.MultiSelectRecyclerViewAdapter;
import com.example.wolf_z.bookingroom.R;


public class ParticipantFragment extends Fragment implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {

    private final int REQ_CODE_participant_search = 12345;
    protected ProgressDialog prgDialog;
    protected Button search_participant_button;
    protected RecyclerView selected_recyclerview;
    private MultiSelectRecyclerViewAdapter item_selected_Adapter;
    protected Createbooking createbooking;

    public ParticipantFragment(Createbooking createbooking) {
        this.createbooking = createbooking;
    }

    public ParticipantFragment() {

    }

    public MultiSelectRecyclerViewAdapter getItem_selected_Adapter() {
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
        selected_recyclerview.setHasFixedSize(true);
        selected_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

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

    @Override
    public void onItemClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        item_selected_Adapter.toggleSelection(position);
    }

    public void setAdapter() {
        item_selected_Adapter = new MultiSelectRecyclerViewAdapter(getActivity(), createbooking.getAccountBeen_selected(), ParticipantFragment.this);
        selected_recyclerview.setAdapter(item_selected_Adapter);
    }

}
