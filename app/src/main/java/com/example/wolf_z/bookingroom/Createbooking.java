package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Createbooking extends Activity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TIME_PATTERN = "HH:mm";

    private TextView txtdate;
    private TextView txttime;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private Button btnsetDate;
    private Button btnsetTime;
    private Button btnsubmit;
    private Animation anim;
    private View view_subject;
    private View view_detail;
    private View view_search;
    private EditText ETsubject;
    private EditText ETdetail;
    private EditText ETsearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createbooking);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);

        anim = AnimationUtils.loadAnimation(Createbooking.this, R.anim.scale);

        view_subject = findViewById(R.id.view_subject);
        view_detail = findViewById(R.id.view_detail);
        view_search = findViewById(R.id.view_search);

        ETsubject = (EditText) findViewById(R.id.ETsubject);
        ETsubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_subject.setVisibility(View.VISIBLE);
                    view_subject.startAnimation(anim);
                } else {
                    view_subject.setVisibility(View.GONE);
                }
            }
        });

        ETdetail = (EditText) findViewById(R.id.ETdetail);
        ETdetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_detail.setVisibility(View.VISIBLE);
                    view_detail.startAnimation(anim);
                } else {
                    view_detail.setVisibility(View.GONE);
                }
            }
        });

        ETsearch = (EditText) findViewById(R.id.ETsearch);
        ETsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_search.setVisibility(View.VISIBLE);
                    view_search.startAnimation(anim);
                } else {
                    view_search.setVisibility(View.GONE);
                }
            }
        });

        btnsetDate = (Button) findViewById(R.id.btnsetDate);
        btnsetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance(Createbooking.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsetTime = (Button) findViewById(R.id.btnsetTime);
        btnsetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(Createbooking.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

            }
        });

        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainBookingActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Create booking complete", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        update();

    }


    private void update() {
        txtdate.setText(dateFormat.format(calendar.getTime()));
        txttime.setText(timeFormat.format(calendar.getTime()));
    }

    public void getdatetime() {
        txtdate.getText();
        txttime.getText();
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

}
