package com.example.wolf_z.bookingroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookingDetail_Offline extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail__offline);

        Button go_app = (Button) findViewById(R.id.go_app);
        TextView txtsubject = (TextView) findViewById(R.id.subject_offline);
        TextView txtdetail = (TextView) findViewById(R.id.detail_offline);
        TextView txtmeetingtype = (TextView) findViewById(R.id.meetingtype_offline);
        TextView txtdate = (TextView) findViewById(R.id.txtdate_offline);
        TextView txtstarttime = (TextView) findViewById(R.id.txtstarttime_offline);
        TextView txtendtime = (TextView) findViewById(R.id.txtendtime_offline);
        TextView txtroomid = (TextView) findViewById(R.id.roomid_offline);
        TextView txtprojectorid = (TextView) findViewById(R.id.projectorid_offline);

        Bundle bundle = getIntent().getExtras();
        int bookingid = bundle.getInt("bookingid");
        String subject = bundle.getString("subject");
        String detail = bundle.getString("detail");
        String meetingtype = bundle.getString("meetingtype");
        String date = bundle.getString("date");
        String starttime = bundle.getString("starttime");
        String endtime = bundle.getString("endtime");
        int roomid = bundle.getInt("roomid");
        int projector = bundle.getInt("projector");

        txtsubject.setText(subject);
        txtdetail.setText(detail);
        txtmeetingtype.setText(meetingtype);
        txtdate.setText(date);
        txtstarttime.setText(starttime);
        txtendtime.setText(endtime);
        txtroomid.setText(String.valueOf(roomid));
        if (projector == 0) {
            txtprojectorid.setText("unselected");
        } else {
            txtprojectorid.setText(String.valueOf(projector));
        }


        go_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
