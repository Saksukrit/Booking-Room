package com.example.wolf_z.bookingroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BookingDetail_Offline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail__offline);

        Bundle bundle = getIntent().getExtras();
        int bookingid = bundle.getInt("bookingid");
        String subject = bundle.getString("subject");

        Toast.makeText(this, "bookingid : " + bookingid + " |||  Subject : " + subject, Toast.LENGTH_LONG).show();

    }
}
