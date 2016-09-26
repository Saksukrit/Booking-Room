package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.RoomBean;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_search;
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
import java.util.Locale;

public class SearchBookActivity extends AppCompatActivity {

    private BookBean bookBean = new BookBean();
    private ArrayList<BookBean> bookBeans = new ArrayList<BookBean>();
    private RoomBean roomBean = new RoomBean();
    private ArrayList<RoomBean> roomBeans = new ArrayList<RoomBean>();
    private ProgressDialog prgDialog;
    private ActionBar actionBar;
    private TextView date;
    private Spinner timeHr;
    private Spinner timeMin;
    private Spinner totimeHr;
    private Spinner totimeMin;
    private Spinner room;
    private Button search;
    private Button reset;
    private TextView txtstatus;
    private ListView searchlist;
    private String settime;
    private String settotime;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatSend;
    private String[] Ssubject;
    private String[] Sdate;
    private String[] Sstarttime;
    private String[] Sendtime;
    private int[] Sroomid;
    private SimpleDateFormat dateFormatter1;
    private CustomAdapter_search adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchlist = (ListView) findViewById(R.id.searchlist);
        search = (Button) findViewById(R.id.btnsearch);
        reset = (Button) findViewById(R.id.btnreset);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        txtstatus.setText("please search");

        /** Date picker*/
        date = (TextView) findViewById(R.id.date);

        Calendar caledar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                date.setText(dateFormatter.format(newDate.getTime()));  /****format*/

            }
        }, caledar.get(Calendar.YEAR), caledar.get(Calendar.MONTH), caledar.get(Calendar.DAY_OF_MONTH));


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        /** Time */
        final ArrayList<String> hr = new ArrayList<String>();
        hr.add("8");
        hr.add("9");
        hr.add("10");
        hr.add("11");
        hr.add("12");
        hr.add("13");
        hr.add("14");
        hr.add("15");
        hr.add("16");
        hr.add("17");
        hr.add("18");
        hr.add("19");
        hr.add("20");
        hr.add("21");
        hr.add("22");

        final ArrayList<String> min = new ArrayList<String>();
        min.add("00");
        min.add("15");
        min.add("30");
        min.add("45");

        ArrayAdapter<String> adapterhr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hr);
        adapterhr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptermin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, min);
        adaptermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Time
        timeHr = (Spinner) findViewById(R.id.timeHr);
        timeHr.setAdapter(adapterhr);
        timeMin = (Spinner) findViewById(R.id.timeMin);
        timeMin.setAdapter(adaptermin);
        //ToTime
        totimeHr = (Spinner) findViewById(R.id.totimeHr);
        totimeHr.setAdapter(adapterhr);
        totimeHr.setSelection(adapterhr.getPosition(hr.get(4)));  //set default show
        totimeMin = (Spinner) findViewById(R.id.totimeMin);
        totimeMin.setAdapter(adaptermin);

        /** select room */

        ArrayList<Integer> listroomEmpty = new ArrayList<Integer>();
        listroomEmpty.add(0);
        listroomEmpty.add(1502);
        listroomEmpty.add(1503);

        room = (Spinner) findViewById(R.id.room);
        ArrayAdapter<Integer> adapterroom = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, listroomEmpty);
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room.setAdapter(adapterroom);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtstatus.setText("please search");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] URL = {"http://157.179.8.120:8080/BookingRoomService/searchrest/restservice/searchbooking"};
                /**  Params **/
                try {
                    bookBean.setDate(dateFormatSend.format(dateFormatter1.parse(date.getText().toString())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookBean.setStarttime(setStartTime());
                bookBean.setEndtime(setEndTime());
                bookBean.setRoomid(Integer.parseInt(room.getSelectedItem().toString()));
                //adapter.notifyDataSetInvalidated();
                new Search().execute(URL);
            }
        });

        /** room spinner Query */
//        String[] URL = {"http://localhost:8080/BookingRoomService/searchrest/restservice/searchbooking"};
//        //  Params book
//        participant.setUsername(username);
//        participant.setBookingid(Integer.parseInt(bookingid));
//        new Search().execute(URL);

    }

    // setTime **/
    public String setStartTime() {
        settime = timeHr.getSelectedItem() + ":" + timeMin.getSelectedItem() + ":00";
        return settime;
    }

    public String setEndTime() {
        settotime = totimeHr.getSelectedItem() + ":" + totimeMin.getSelectedItem() + ":00";
        return settotime;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem mnu1 = menu.add(0, 0, 0, "Go Create");
        {
            mnu1.setIcon(R.drawable.book_icon);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "Go Create", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, Createbooking.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
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
     * Search Task
     */
    private class Search extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
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

            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BookBean bookBean = new BookBean();
                    bookBean.setSubject(jsonObject.getString("subject"));
                    bookBean.setDate(jsonObject.getString("date"));
                    bookBean.setStarttime(jsonObject.getString("starttime"));
                    bookBean.setEndtime(jsonObject.getString("endtime"));
                    bookBean.setRoomid(jsonObject.getInt("roomid"));
                    bookBeans.add(bookBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (bookBeans.size() == 0) {
                Ssubject = new String[]{"empty"};
                Sdate = new String[]{"00/00/0000"};
                Sstarttime = new String[]{"00:00"};
                Sendtime = new String[]{"00:00"};
                Sroomid = new int[]{0};
            } else {
                Ssubject = new String[bookBeans.size()];
                Sdate = new String[bookBeans.size()];
                Sstarttime = new String[bookBeans.size()];
                Sendtime = new String[bookBeans.size()];
                Sroomid = new int[bookBeans.size()];

                for (int i = 0; i < bookBeans.size(); i++) {
                    Ssubject[i] = bookBeans.get(i).getSubject();
                    Sdate[i] = bookBeans.get(i).getDate();
                    Sstarttime[i] = bookBeans.get(i).getStarttime();
                    Sendtime[i] = bookBeans.get(i).getEndtime();
                    Sroomid[i] = bookBeans.get(i).getRoomid();
                }
            }
            //set adapter
            adapter = new CustomAdapter_search(getApplicationContext(), Ssubject, Sdate, Sstarttime, Sendtime, Sroomid);
            searchlist.setAdapter(adapter);
            bookBeans.clear();
        }

    }

}
