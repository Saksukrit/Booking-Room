package com.example.wolf_z.bookingroom;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Createbooking.Createbooking;
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
import java.util.Objects;

public class SearchBookActivity extends AppCompatActivity {

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private BookBean bookBean_select = new BookBean();
    private ArrayList<BookBean> bookBeans_to_list = new ArrayList<>();
    private ArrayList<RoomBean> roomBeans = new ArrayList<>();
    private ProgressDialog prgDialog;
    protected ActionBar actionBar;
    private TextView date_show;
    private String date_send;
    private Spinner timeHr;
    private Spinner timeMin;
    private Spinner totimeHr;
    private Spinner totimeMin;
    protected Spinner room;
    protected Button search;
    protected Button reset;
    private TextView txtstatus;
    private ListView searchlist;
    protected String settime;
    protected String settotime;
    private ArrayList<String> Aroom = new ArrayList<>();

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private SimpleDateFormat timeFormatter2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private SimpleDateFormat dateFormatShow = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    protected CustomAdapter_search adapter;

    private String intent_date;
    private String intent_starttime;
    private String intent_endtime;
    private int intent_roomid;

    private boolean searchstatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchlist = (ListView) findViewById(R.id.searchlist);
        search = (Button) findViewById(R.id.btnsearch);
        reset = (Button) findViewById(R.id.btnreset);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        txtstatus.setText("please search_button");

        /** Date picker*/
        date_show = (TextView) findViewById(R.id.date);

        Calendar caledar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                /****format*/
                date_show.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year + 543));
                date_send = dateFormatSend.format(newDate.getTime());

            }
        }, caledar.get(Calendar.YEAR), caledar.get(Calendar.MONTH), caledar.get(Calendar.DAY_OF_MONTH));


        date_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        /** Time */
        final ArrayList<String> hr = new ArrayList<>();
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

        final ArrayList<String> min = new ArrayList<>();
        min.add("00");
        min.add("15");
        min.add("30");
        min.add("45");

        ArrayAdapter<String> adapterhr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hr);
        adapterhr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptermin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, min);
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

        /** room_spinner spinner Query */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/searchrest/restservice/getroom"};
        new SetData().execute(URL);

        room = (Spinner) findViewById(R.id.room);
        Aroom.add("unselect");
        ArrayAdapter<String> adapterroom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Aroom);
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room.setAdapter(adapterroom);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookBeans_to_list.clear();
                txtstatus.setBackgroundColor(0xffffff00);
                txtstatus.setText("please search_button");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookBeans_to_list.clear();
                searchstatus = true;
                if (Objects.equals(date_show.getText().toString(), "click to get date")) {
                    Snackbar.make(v, "Unselected Date", Snackbar.LENGTH_SHORT).show();
                } else {
                    //check case room : selected/unselected
                    if (Objects.equals(room.getSelectedItem().toString(), "unselect")) {
                        bookBean_select.setRoomid(0);
                        Snackbar.make(v, "Unselected Room", Snackbar.LENGTH_SHORT).show();
                    } else {
                        bookBean_select.setRoomid(Integer.parseInt(room.getSelectedItem().toString()));
                    }

                    String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/searchrest/restservice/searchbooking"};
                    /**  Params **/
                    try {
                        bookBean_select.setDate(date_send);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bookBean_select.setStarttime(setStartTime());
                    bookBean_select.setEndtime(setEndTime());
                    new Search().execute(URL);
                }
            }
        });
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
                if (!searchstatus) {
                    Snackbar.make(new View(this), "You not search", Snackbar.LENGTH_LONG);
                } else if (bookBeans_to_list.size() == 0) {
                    Toast.makeText(this, "Go Create", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, Createbooking.class);
                    intent.putExtra("search_intent_date", intent_date);
                    intent.putExtra("search_intent_starttime", intent_starttime);
                    intent.putExtra("search_intent_endtime", intent_endtime);
                    intent.putExtra("search_intent_roomid", intent_roomid);
                    intent.putExtra("from", "search_booking");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(new View(this), "Unable create on this time", Snackbar.LENGTH_LONG);
                }
                return true;
            case android.R.id.home:
                finish();
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
//                } else {
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
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
                StringEntity stringEntity = new StringEntity(gson.toJson(bookBean_select));
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
                    bookBean.setDate(jsonObject.getString("date"));
                    bookBean.setStarttime(jsonObject.getString("starttime"));
                    bookBean.setEndtime(jsonObject.getString("endtime"));
                    bookBean.setRoomid(jsonObject.getInt("roomid"));
                    bookBeans_to_list.add(bookBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (bookBeans_to_list.size() == 0) {
                txtstatus.setBackgroundColor(0xff00ff00);
                txtstatus.setText("Room is empty this time.");
                /**set value intent*/
                try {
                    intent_date = date_show.getText().toString();
                    intent_starttime = timeFormatter.format(timeFormatter2.parse(setStartTime()));
                    intent_endtime = timeFormatter.format(timeFormatter2.parse(setEndTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent_roomid = bookBean_select.getRoomid();
            } else {
                txtstatus.setBackgroundColor(0xffff0000);
                txtstatus.setText("Room is not empty this time.");
                //set adapter
                adapter = new CustomAdapter_search(getApplicationContext(), bookBeans_to_list);
                searchlist.setAdapter(adapter);
            }

        }

    }


    /**
     * Set Data Page
     */
    private class SetData extends AsyncTask<String, Void, String> {

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
                StringEntity stringEntity = new StringEntity(gson.toJson(""));
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
                    RoomBean roomBean = new RoomBean();
                    roomBean.setRoomid(jsonObject.getInt("roomid"));
                    roomBeans.add(roomBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < roomBeans.size(); i++) {
                Aroom.add(String.valueOf(roomBeans.get(i).getRoomid()));

            }

        }

    }

}
