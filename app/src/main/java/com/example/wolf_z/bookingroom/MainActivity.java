package com.example.wolf_z.bookingroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    Button createbooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createbooking = (Button) findViewById(R.id.createbooking);


        String[] list = {"Aerith Gainsborough", "Barret Wallace", "Cait Sith"
                , "Cid Highwind", "Cloud Strife", "RedXIII", "Sephiroth"
                , "Tifa Lockhart", "Vincent Valentine", "Yuffie Kisaragi"
                , "ZackFair"};

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
        Intent intent = new Intent(getApplicationContext(), Createbooking1.class);
        startActivity(intent);
        finish();
    }

}