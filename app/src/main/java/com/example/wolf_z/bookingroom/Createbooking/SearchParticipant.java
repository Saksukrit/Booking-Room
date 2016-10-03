package com.example.wolf_z.bookingroom.Createbooking;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Createbooking.Multi_Search.MultiSelectRecyclerViewAdapter;
import com.example.wolf_z.bookingroom.R;

import java.util.ArrayList;

public class SearchParticipant extends AppCompatActivity implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {
    private ActionBar actionBar;
    private ArrayList<String> list_participant = new ArrayList<>();

    private RecyclerView recyclerView;
    private MultiSelectRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_participant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        list_participant.add("Adela ");
        list_participant.add("Ichiko ");
        list_participant.add("Patricia ");
        list_participant.add("Rachael ");
        list_participant.add("Veronica ");
        list_participant.add("Iris ");
        list_participant.add("Uriana ");
        list_participant.add("Valencia ");
        list_participant.add("Alexandra ");
        list_participant.add("Adriana ");

        recyclerView = (RecyclerView) findViewById(R.id.list_participant);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiSelectRecyclerViewAdapter(SearchParticipant.this, list_participant, this);
        recyclerView.setAdapter(mAdapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                String s = "";
                /**loop get data by position mapping index*/
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    for (int j = 0; j < mAdapter.getSelectedItemCount(); j++) {
                        if (i == mAdapter.getSelectedItems().get(j)) {
                            s += list_participant.get(i) + " ,";
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                return true;
            case 1:
                mAdapter.clearSelection();
                if (mAdapter.getSelectedItemCount() == 0) {
                    Toast.makeText(getApplicationContext(), "clear", Toast.LENGTH_LONG).show();
                }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem mnu1 = menu.add(0, 0, 0, "get");
        {
            mnu1.setIcon(R.drawable.create512);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        MenuItem mnu2 = menu.add(1, 1, 1, "clear");
        {
            mnu2.setTitle("clear");
            mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }
}
