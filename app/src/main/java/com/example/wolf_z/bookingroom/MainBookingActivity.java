package com.example.wolf_z.bookingroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class MainBookingActivity extends AppCompatActivity {
    private Button createbooking;


//    JSONParser jParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createbooking = (Button) findViewById(R.id.createbooking);


        String[] list = {"JPS", "Docker", "UI UX"
                , "Servlet", "Kotlin", "Project"};

        CustomAdapter_subject adapter = new CustomAdapter_subject(getApplicationContext(), list);

        ListView listView = (ListView) findViewById(R.id.subjectlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BookingDetail.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onClickcreatebooking(View view) {
        Intent intent = new Intent(getApplicationContext(), Createbooking.class);
        startActivity(intent);
        finish();
    }

}