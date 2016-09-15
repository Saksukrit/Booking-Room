package com.example.wolf_z.bookingroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.wolf_z.bookingroom.Custom.CustomAdapter_Pname;

public class BookingDetail extends AppCompatActivity {
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail);

        back = (Button) findViewById(R.id.back);

        String[] list = {"Harry", "Oliver", "Jack"
                , "Alfie", "Charlie", "Thomas", "Jacob"
                , "James", "William", "Daniel"
                , "Riley"};

        CustomAdapter_Pname adapter = new CustomAdapter_Pname(getApplicationContext(), list);

        ListView listView = (ListView) findViewById(R.id.Pnamelist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), MainBookingActivity.class);
        startActivity(intent);
        finish();
    }

}
