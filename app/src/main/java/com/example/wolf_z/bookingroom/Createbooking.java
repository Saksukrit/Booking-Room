package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Createbooking extends Activity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TIME_PATTERN = "HH:mm";
    private ProgressDialog prgDialog;
    private TextView txtdate;
    private TextView txttime;
    private TextView txttotime;
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
    private BookBean bookBean = new BookBean();
    private RadioGroup meeting_type;
    private RadioButton meetingButton;
    private Spinner department_type;

    private String setDate;
    private String setTime;
    private String setToTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createbooking);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        txtdate = (TextView) findViewById(R.id.txtdate);
        setDate = txtdate.getText().toString();

        txttime = (TextView) findViewById(R.id.txttime);
        setTime = txttime.getText().toString();

        txttotime = (TextView) findViewById(R.id.txttotime);
        setToTime = txttotime.getText().toString();

        /** meeting_type */
        meeting_type = (RadioGroup) findViewById(R.id.meeting_type);
        int meetingselectedId = meeting_type.getCheckedRadioButtonId();
        meetingButton = (RadioButton) findViewById(meetingselectedId);


        /** department_type */
        department_type = (Spinner) findViewById(R.id.department_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.department_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_type.setAdapter(adapter);
//        department_type.getOnItemSelectedListener().toString();

        /** AnimationUtils */
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
//                String URL = "http://157.179.8.120:8080/BookingRoomService/bookingrest/restservice/dobooking";
                /**  Params **/
                bookBean.setSubject(ETsubject.getText().toString());
                bookBean.setMeeting_type(meetingButton.getText().toString());
                bookBean.setDate("");
                bookBean.setTime("");
                bookBean.setTotime("");
                bookBean.setDetail(ETdetail.getText().toString());
                bookBean.setProjid(1);
                bookBean.setRoomid(1);


//                new doCreateBooking().execute(URL);

//                Intent intent = new Intent(getApplicationContext(), MainBookingActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Create booking complete", Toast.LENGTH_LONG).show();
//                finish();
            }
        });


        update();

    }


    private void update() {
        txtdate.setText(dateFormat.format(calendar.getTime()));
        txttime.setText(timeFormat.format(calendar.getTime()));
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

    /****************************************************************************/
    private class doCreateBooking extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);


                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(bookBean));

                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity);
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.dismiss();

            String tag = "";
            String status = "";
            String error_msg = "";
            try {

                JSONObject jsonObject = new JSONObject(result);

                tag = jsonObject.getString("tag").toString();
                status = jsonObject.getString("status").toString();
                error_msg = jsonObject.getString("error_msg").toString();


            } catch (JSONException e) {

                e.printStackTrace();
            }

            if (error_msg == "") {
                String success = "Create Book Success!";
                Toast toast = Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                String OutputData = " Ops! : Booking " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getBaseContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }


}
