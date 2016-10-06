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
import android.widget.ListView;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Createbooking.Multi_Search.MultiSelectRecyclerViewAdapter;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;


public class ParticipantFragment extends Fragment implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {

    private ProgressDialog prgDialog;
    private Button search_participant;

    private ArrayList<AccountBean> accountBeens = new ArrayList<>();
    protected RecyclerView selected_list;
    private MultiSelectRecyclerViewAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_createbooking_participant, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);


        AccountBean accountBean = new AccountBean();
        accountBean.setDisplayname("oooooo");
        accountBean.setDepartment("mobile");
        accountBean.setUsername("fdfdf");
        accountBeens.add(accountBean);

        selected_list = (RecyclerView) view.findViewById(R.id.selected_list);
        selected_list.setHasFixedSize(true);
        selected_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MultiSelectRecyclerViewAdapter(getActivity(), accountBeens, ParticipantFragment.this);
        selected_list.setAdapter(mAdapter);


        search_participant = (Button) view.findViewById(R.id.search_participant);
        search_participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ParticipantSearch.class);
                startActivity(intent);
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
        mAdapter.toggleSelection(position);
    }
}
