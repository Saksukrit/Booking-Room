package com.example.wolf_z.bookingroom.Createbooking;


import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.DepartmentBean;
import com.example.wolf_z.bookingroom.Bean.RoomBean;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.MainBookingActivity;
import com.example.wolf_z.bookingroom.R;
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
import java.util.ArrayList;


public class Createbooking extends AppCompatActivity {

    private ProgressDialog prgDialog;
    protected ActionBar actionBar;
    protected ActionBar.Tab tabsubject, tabparticipant;
    private Fragment subjectFragment = new SubjectFragment();
    private Fragment participantFragment = new ParticipantFragment();
    private BookBean bookBean = new BookBean();

    private String testcom = "ggggggggggggggggggggggg";

    public String getTestcom() {
        return testcom;
    }

    public void setTestcom(String testcom) {
        this.testcom = testcom;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbooking);
        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        tabsubject = actionBar.newTab().setText("Subject").setTabListener(new TabListener(subjectFragment));
        tabparticipant = actionBar.newTab().setText("Participant").setTabListener(new TabListener(participantFragment));

        actionBar.addTab(tabsubject);
        actionBar.addTab(tabparticipant);

    }

    /**
     * NeviBar Setting
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem mnu1 = menu.add(0, 0, 0, "Create");
        {
            mnu1.setIcon(R.drawable.create512);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    /**
     * create Button on nevi
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(getApplicationContext(), testcom, Toast.LENGTH_LONG).show();
//                //condition check input null data on UI
//                if (room.getSelectedItem() == "unselect") {
//                    Toast.makeText(this, "Room Unselected", Toast.LENGTH_LONG).show();
//                } else {
//
//                    Toast.makeText(this, "Create booking", Toast.LENGTH_LONG).show();
//                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/dobooking";
//                    /**  Params **/
//                    bookBean.setSubject(ETsubject.getText().toString());
//                    bookBean.setMeeting_type(meetingButton.getText().toString());
//                    try {
//                        bookBean.setDate(dateFormatSend.format(dateFormatter1.parse(date.getText().toString())));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    bookBean.setStarttime(setTime());
//                    bookBean.setEndtime(setToTime());
//                    bookBean.setDetail(ETdetail.getText().toString());
//                    bookBean.setRoomid(Integer.parseInt(room.getSelectedItem().toString()));
//                    bookBean.setProjid(Integer.parseInt(projector.getSelectedItem().toString()));
//                    new doCreateBooking().execute(URL);
//                    Intent intent = new Intent(getApplicationContext(), MainBookingActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(), "Create booking complete", Toast.LENGTH_LONG).show();
//                    finish();
//                    return true;
//                }
//                return false;
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return false;
    }


    /**
     * Create Booking
     */
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
                Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                String OutputData = " Ops! : Booking " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

}
