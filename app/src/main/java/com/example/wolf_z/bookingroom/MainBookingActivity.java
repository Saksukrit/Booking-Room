package com.example.wolf_z.bookingroom;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.Participant;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_subject;
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
import java.util.Date;
import java.util.Locale;


public class MainBookingActivity extends AppCompatActivity {
    private ProgressDialog prgDialog;
    Participant participant = new Participant();
    private Button createbooking;
    private Bundle bundle;
    private String username;
    private String date;
    private String subject;
    private String bookingid;
    private ArrayList<BookBean> bookBeans = new ArrayList<BookBean>();
    private String[] Ssubject;
    private String[] Sdate;
    private String[] Stime;
    private int[] Sbookingid;
    private int[] Sroomid;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = getIntent().getExtras();
//        username = bundle.getString("username");
        username = "rrr";

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.subjectlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BookingDetail.class);
                intent.putExtra("username", username);
                intent.putExtra("bookingid", bookingid);
                startActivity(intent);
            }
        });

        /** Query */
        String URL = "http://157.179.8.120:8080/BookingRoomService/mainrest/restservice/showlistsubject";
        //  Params
        participant.setUsername(username);
        new MainApp().execute(URL);


        createbooking = (Button) findViewById(R.id.createbooking);
        createbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Createbooking.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });


    }

    private class MainApp extends AsyncTask<String, Void, String> {

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

        @Override
        protected void onPostExecute(String result) {
            prgDialog.dismiss();

            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BookBean bookBean = new BookBean();
                    bookBean.setSubject(jsonObject.getString("subject"));
                    bookBean.setBookingid(jsonObject.getInt("bookingid"));
                    bookBean.setDate(jsonObject.getString("date"));
                    bookBean.setStarttime(jsonObject.getString("starttime"));
                    bookBean.setRoomid(jsonObject.getInt("roomid"));
                    bookBeans.add(bookBean);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (bookBeans.size() == 0) {
                Ssubject = new String[]{"empty"};
                Sdate = new String[]{"00/00/0000"};
                Stime = new String[]{"00:00"};
                Sbookingid = new int[]{0};
                Sroomid = new int[]{0};
            } else {
                Ssubject = new String[bookBeans.size()];
                Sdate = new String[bookBeans.size()];
                Stime = new String[bookBeans.size()];
                Sbookingid = new int[bookBeans.size()];
                Sroomid = new int[bookBeans.size()];

                for (int i = 0; i < bookBeans.size(); i++) {
                    Ssubject[i] = bookBeans.get(i).getSubject();
                    Sdate[i] = bookBeans.get(i).getDate();
                    Stime[i] = bookBeans.get(i).getStarttime();
                    Sbookingid[i] = bookBeans.get(i).getBookingid();
                    Sroomid[i] = bookBeans.get(i).getRoomid();
                }
            }

            CustomAdapter_subject adapter = new CustomAdapter_subject(getApplicationContext(), Ssubject, Sdate, Stime, Sbookingid, Sroomid);
            listView.setAdapter(adapter);


        }
    }
}