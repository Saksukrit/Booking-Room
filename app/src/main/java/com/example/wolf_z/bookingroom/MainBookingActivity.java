package com.example.wolf_z.bookingroom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.Participant;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Createbooking.Createbooking;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_subject;
import com.example.wolf_z.bookingroom.Menu_Nevigator.AboutActivity;
import com.example.wolf_z.bookingroom.Menu_Nevigator.Profile_Setting.Profile_Setting_Activity;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainBookingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private Participant participant = new Participant();
    private AccountBean accountBean = new AccountBean();
    protected Button createbooking;
    protected Button searchbooking;
    protected Bundle bundle;
    private String username;
    private String bookingid;
    private ArrayList<BookBean> bookBeans = new ArrayList<>();
    private ListView listView;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bundle = getIntent().getExtras();
//       username = bundle.getString("username");
        username = "krit025";
        accountBean.setUsername(username);

        ProgressDialog prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        listView = (ListView) findViewById(R.id.subjectlist);


        /** Toolbar ********************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        /** Query */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/showlistsubject"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/getprofile"};
        //  Params
        participant.setUsername(username);
        new MainApp().execute(URL);


        createbooking = (Button) findViewById(R.id.createbooking);
        createbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Createbooking.class);
                intent.putExtra("username", username);
                intent.putExtra("from", "main");
                startActivityForResult(intent, 1);
            }
        });

        searchbooking = (Button) findViewById(R.id.searchbooking);
        searchbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                bookingid = String.valueOf(bookBeans.get(position).getBookingid());
                Intent intent = new Intent(getApplicationContext(), BookingDetail.class);
                intent.putExtra("username", username);
                intent.putExtra("bookingid", bookingid);
                intent.putExtra("checkfrommain", "main");
                startActivityForResult(intent, 1);
            }
        });


    }

    public void RefreshMain_comeback() {
        Intent intent = getIntent();
        intent.putExtra("username", username);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RefreshMain_comeback();
    }

    /**
     * Navigation
     **/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            Toast.makeText(getApplicationContext(), "about", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.profilesetting) {
            Toast.makeText(getApplicationContext(), "setting", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Profile_Setting_Activity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1);
        } else if (id == R.id.logout) {
            Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void set_Subject_Notification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        Intent notificationIntent_clear = new Intent("Alarm_booking_room_service");
        notificationIntent_clear.addCategory("android.intent.category.DEFAULT");
        Intent notificationIntent = new Intent("Alarm_booking_room_service");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_month = new SimpleDateFormat("MM");
        SimpleDateFormat format_day = new SimpleDateFormat("dd");

        SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format_Hr = new SimpleDateFormat("HH");
        SimpleDateFormat format_Min = new SimpleDateFormat("mm");

        //clear all
        Calendar calendar = Calendar.getInstance();   //this time
        notificationIntent_clear.putExtra("command", "clear_all_notification");
        PendingIntent broadcast_clear = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent_clear, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast_clear);


        for (int i = 0; i < bookBeans.size(); i++) {
            Date date_check = new Date();
            Date date_now = new Date();
            String date_yser = null;
            String date_month = null;
            String date_day = null;
            String date_pre_send;
            String date_hr = null;
            String date_min = null;
            try {
                date_yser = String.valueOf(format_year.format(format_date.parse(bookBeans.get(i).getDate())));
                date_yser = String.valueOf(Integer.valueOf(date_yser) - 543);
                date_month = String.valueOf(format_month.format(format_date.parse(bookBeans.get(i).getDate())));
                date_day = String.valueOf(format_day.format(format_date.parse(bookBeans.get(i).getDate())));
                date_pre_send = date_yser + "/" + date_month + "/" + date_day;

                date_hr = String.valueOf(format_Hr.format(format_time.parse(bookBeans.get(i).getStarttime())));
                date_min = String.valueOf(format_Min.format(format_time.parse(bookBeans.get(i).getStarttime())));

                date_check = format.parse(date_pre_send + " " + (Integer.valueOf(date_hr) - 1) + ":" + date_min);
                Log.d("check ", String.valueOf(date_check));
                date_now = format.parse(format.format(calendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //check before/after date time ,before 1 hr.
            if (date_now.before(date_check)) {
                Log.d("this time before ", bookBeans.get(i).getDate() + " " + date_hr + ":" + date_min);
                //notify_id
                int notify_id = bookBeans.get(i).getBookingid();
                notificationIntent.putExtra("notify_id", notify_id);
                notificationIntent.putExtra("command", "create_notification");
                //data
                notificationIntent.putExtra("bookingid", bookBeans.get(i).getBookingid());
                notificationIntent.putExtra("subject", bookBeans.get(i).getSubject());
                notificationIntent.putExtra("detail", bookBeans.get(i).getDetail());
                notificationIntent.putExtra("meetingtype", bookBeans.get(i).getMeeting_type());
                notificationIntent.putExtra("date", bookBeans.get(i).getDate());
                notificationIntent.putExtra("starttime", bookBeans.get(i).getStarttime());
                notificationIntent.putExtra("endtime", bookBeans.get(i).getEndtime());
                notificationIntent.putExtra("roomid", bookBeans.get(i).getRoomid()); //int
                notificationIntent.putExtra("projector", bookBeans.get(i).getProjid()); //int

                cal.set(Integer.parseInt(date_yser), Integer.parseInt(date_month) - 1, Integer.parseInt(date_day), Integer.parseInt(date_hr) - 1, Integer.parseInt(date_min));
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), notify_id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            } else {
                Log.d("this time after ", bookBeans.get(i).getDate() + " " + date_hr + ":" + date_min);
            }


        }
    }

    /**************************************************************/

    private class MainApp extends AsyncTask<String, Void, String[]> {

        String[] result = {};

        String doIn_listSubject(String... urls) {
            String result = "";
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(participant));
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

        String doIn_getprofile(String... urls) {
            String result = "";
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(accountBean));
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
        protected String[] doInBackground(String... urls) {
            result = new String[urls.length];
            result[0] = doIn_listSubject(urls[0]);
            result[1] = doIn_getprofile(urls[1]);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result[0]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BookBean bookBean = new BookBean();
                    bookBean.setBookingid(jsonObject.getInt("bookingid"));
                    bookBean.setSubject(jsonObject.getString("subject"));
                    bookBean.setDetail(jsonObject.getString("detail"));
                    bookBean.setDate(jsonObject.getString("date"));
                    bookBean.setMeeting_type(jsonObject.getString("meeting_type"));
                    bookBean.setStarttime(jsonObject.getString("starttime"));
                    bookBean.setEndtime(jsonObject.getString("endtime"));
                    bookBean.setRoomid(jsonObject.getInt("roomid"));
                    bookBean.setProjid(jsonObject.getInt("projid"));
                    bookBeans.add(bookBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomAdapter_subject adapter = new CustomAdapter_subject(getApplicationContext(), bookBeans);
            listView.setAdapter(adapter);

            /*** get profile ********/
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result[1]);

                accountBean.setUsername(jsonObject.getString("username"));
                accountBean.setDisplayname(jsonObject.getString("displayname"));
                accountBean.setDepartment(jsonObject.getString("department"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView profile_displayname = (TextView) header.findViewById(R.id.profile_displayname);
            profile_displayname.setText(accountBean.getDisplayname());

            TextView profile_department = (TextView) header.findViewById(R.id.profile_department);
            profile_department.setText(accountBean.getDepartment());

            //set notification
            set_Subject_Notification();
        }
    }

}