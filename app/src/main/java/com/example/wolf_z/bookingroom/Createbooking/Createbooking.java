package com.example.wolf_z.bookingroom.Createbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.Participant;
import com.example.wolf_z.bookingroom.Bean.ParticipantArray;
import com.example.wolf_z.bookingroom.Bean.RoomBean;
import com.example.wolf_z.bookingroom.BookingDetail;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class Createbooking extends AppCompatActivity {

    public static final String participantarray_KEY = "participantarray";
    private final int REQ_CODE_participant_search = 12345;

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private ProgressDialog prgDialog;
    protected ActionBar actionBar;
    private SubjectFragment subjectFragment = new SubjectFragment(this);
    private ParticipantFragment participantFragment = new ParticipantFragment(this);
    private BookBean bookBean_to_create = new BookBean();
    private ParticipantArray participant_to_create = new ParticipantArray();

    protected SimpleDateFormat dateFormatter;
    protected SimpleDateFormat dateFormatSend;
    protected SimpleDateFormat dateFormatter1;

    public static ArrayList<AccountBean> accountBeen_selected_arraylist = new ArrayList<>();
    private ArrayList<String> roomshow_spinner_arraylist = new ArrayList<>();

    //params edit
    protected Bundle bundle;
    private String bookingid = "";
    private String status = "";
    private ArrayList<BookBean> bookBeans_edit = new ArrayList<>();
    private ArrayList<AccountBean> accountBeans_edit = new ArrayList<>();
    private Participant participant_edit = new Participant();

    private String from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearArraylist();

        setContentView(R.layout.activity_createbooking);
        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        actionBar = getSupportActionBar();

        /** Tab */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Subject"));
        tabLayout.addTab(tabLayout.newTab().setText("Participant"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /** ViewPager */
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        roomshow_spinner_arraylist.add("unselect");
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/searchrest/restservice/getroom"};
        new SetData().execute(URL);


        /**check from detail to edit*/
        from = this.getIntent().getExtras().getString("from");
        if (Objects.equals(from, "detail_to_edit")) {
            bundle = getIntent().getExtras();
            bookingid = bundle.getString("bookingid");
            participant_edit.setBookingid(Integer.parseInt(bookingid));

            status = bundle.getString("status");
            if (Objects.equals(status, "Edit Booking")) {
                actionBar.setTitle(status);
            }

            /** Query */
            String[] URLdetail = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/showdetail"
                    , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/showpaticipant"};
            new Detail().execute(URLdetail);
        }

        /** check from search_booking to create */
        else if (Objects.equals(from, "search_booking")) {
            bundle = getIntent().getExtras();
            String search_intent_date = bundle.getString("search_intent_date");
            String search_intent_starttime = bundle.getString("search_intent_starttime");
            String search_intent_endtime = bundle.getString("search_intent_endtime");
            int search_intent_roomid = bundle.getInt("search_intent_roomid");

            new SetfromSearch(search_intent_date, search_intent_starttime, search_intent_endtime, search_intent_roomid).execute("");


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "enter onActivityResult");

//        AccountBean x = data.getParcelableExtra(account_selected_KEY);

//        Bundle bundle = data.getExtras();
//        ArrayList<AccountBean> x = bundle.getParcelableArrayList("detailBeanList");

//        accountBeen_selected.addAll(x);

        Log.d("test get data ", String.valueOf(accountBeen_selected_arraylist));

        participantFragment.getItem_selected_Adapter().notifyDataSetChanged();

//        if (requestCode != REQ_CODE_participant_search) return;
//        if (resultCode != RESULT_OK) return;
//
//
////        accountBeen_selected = Parcels.unwrap(data.getParcelableExtra(account_selected_KEY));
//
//
//        if (accountBeen_selected == null) return;

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * NeviBar Setting
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem menu_save = menu.add(0, 0, 0, "save");
        {
            menu_save.setIcon(R.drawable.saveicon512);
            menu_save.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        MenuItem menu_create = menu.add(1, 1, 1, "create");
        {
            menu_create.setIcon(R.drawable.create512);
            menu_create.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        //check create or edit
        if (Objects.equals(status, "Edit Booking")) {
            menu_create.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0://save
                setOptionSave();
                return true;
            case 1://create
                setOptionCreate();
                return true;
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(111, intent);
                finish();
                return true;
        }
        return false;
    }


    /**
     * get set
     */

    public ArrayList<AccountBean> getAccountBeen_selected() {
        return accountBeen_selected_arraylist;
    }

    public ArrayList<String> getRoomshow_spinner_arraylist() {
        return roomshow_spinner_arraylist;
    }

    public SubjectFragment getSubjectFragment() {
        return subjectFragment;
    }

    public ParticipantFragment getParticipantFragment() {
        return participantFragment;
    }


    /**
     * Create Booking
     */
    private class doCreateBooking extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected String doInBackground(String... urls) {
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);


                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(bookBean_to_create));

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

            String tag = "";
            String status = "";
            String error_msg = "";

            try {

                JSONObject jsonObject = new JSONObject(result);

                status = jsonObject.getString("status");
                error_msg = jsonObject.getString("error_msg");
                bookingid = jsonObject.getString("bookingid");   //***********

                if (Objects.equals(error_msg, "")) {
                    String success = "Create Book Success! " + bookingid;
                    Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT);
                    toast.show();

                    /**Params participant_to_create*/

                    String[] username = new String[accountBeen_selected_arraylist.size()];
                    for (int i = 0; i < accountBeen_selected_arraylist.size(); i++) {
                        username[i] = accountBeen_selected_arraylist.get(i).getUsername();
                    }

                    participant_to_create.setBookingid(Integer.parseInt(bookingid));
                    participant_to_create.setUsername(username);
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/doparticipant";
                    new doCreateParticipant().execute(URL);

                } else {//Create Book Unsuccess
                    String OutputData = "Sory!! ,Booking " + status + " "
                            + " ," + error_msg;
                    Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_SHORT);
                    toast.show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Create Participant
     */
    private class doCreateParticipant extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected String doInBackground(String... urls) {
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                Gson gson = new Gson();

                String x = gson.toJson(participant_to_create);
                StringEntity stringEntity = new StringEntity(gson.toJson(participant_to_create));
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

            String status = "";
            String error_msg = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                error_msg = jsonObject.getString("error_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String OutputData = "Participant " + status + " "
                    + " ," + error_msg;
            Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_LONG);
            toast.show();

        }

    }


    /**
     * Set Data Page (room & projector)
     */
    private class SetData extends AsyncTask<String, Void, String[]> {

        String[] result = {};

        String doOn(String... urls) {
            String result = "";
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
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected String[] doInBackground(String... urls) {
            result = new String[urls.length];
            result[0] = doOn(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            JSONArray jsonArray;
            /**room_spinner*/
            try {
                jsonArray = new JSONArray(result[0]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RoomBean roomBean = new RoomBean();
                    roomBean.setRoomid(jsonObject.getInt("roomid"));
                    roomshow_spinner_arraylist.add(String.valueOf(roomBean.getRoomid()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Get Edit data
     */
    private class Detail extends AsyncTask<String, Void, String[]> {

        String[] result = {""};


        String doCon(String url, Participant participant) {
            String result = "";
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
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
        protected String[] doInBackground(String... urls) {
            result = new String[urls.length];

            result[0] = doCon(urls[0], participant_edit);
            result[1] = doCon(urls[1], participant_edit);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {

            /** detail_to_update */
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result[0]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BookBean bookBean = new BookBean();
                    bookBean.setSubject(jsonObject.getString("subject"));
                    bookBean.setMeeting_type(jsonObject.getString("meeting_type"));
                    bookBean.setDate(jsonObject.getString("date"));
                    bookBean.setStarttime(jsonObject.getString("starttime"));
                    bookBean.setEndtime(jsonObject.getString("endtime"));
                    bookBean.setDetail(jsonObject.getString("detail"));
                    bookBean.setRoomid(jsonObject.getInt("roomid"));
                    bookBean.setProjid(jsonObject.getInt("projid"));
                    bookBeans_edit.add(bookBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            subjectFragment.getETsubject().setText(bookBeans_edit.get(0).getSubject());
            setMeeting_type(bookBeans_edit.get(0).getMeeting_type());
            subjectFragment.getETdetail().setText(bookBeans_edit.get(0).getDetail());
            subjectFragment.getDate_show().setText(bookBeans_edit.get(0).getDate());
            setTimeSpinner(bookBeans_edit.get(0).getStarttime(), bookBeans_edit.get(0).getEndtime());
            setRoomSpinner(bookBeans_edit.get(0).getRoomid());
            setProjectorSpinner();//null

            /** participant_to_update*/
            try {
                jsonArray = new JSONArray(result[1]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AccountBean accountBean = new AccountBean();
                    accountBean.setDisplayname(jsonObject.getString("displayname"));
                    accountBean.setDepartment(jsonObject.getString("department"));
                    accountBean.setUsername(jsonObject.getString("username"));
                    accountBeans_edit.add(accountBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            accountBeen_selected_arraylist = accountBeans_edit;
            participantFragment.setAdapter();
        }
    }

    /**
     * Update booking & participant
     */

    private class Update extends AsyncTask<String, Void, String[]> {
        String[] result = {""};

        String doUpdateBooking(String url, BookBean bookBean) {
            String result = "";
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
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

        String doUpdateParticipant(String url, ParticipantArray participantArray) {
            String result = "";
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(participantArray));
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

            result[0] = doUpdateBooking(urls[0], bookBean_to_create);


            String[] username = new String[accountBeen_selected_arraylist.size()];
            for (int i = 0; i < accountBeen_selected_arraylist.size(); i++) {
                username[i] = accountBeen_selected_arraylist.get(i).getUsername();
            }
            participant_to_create.setBookingid(Integer.parseInt(bookingid));
            participant_to_create.setUsername(username);
            result[1] = doUpdateParticipant(urls[1], participant_to_create);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            String error_msgupdate_booking = "";
            String error_msgupdate_participant = "";

            JSONObject jsonObject0;
            JSONObject jsonObject1;
            try {
                jsonObject0 = new JSONObject(result[0]);
                jsonObject1 = new JSONObject(result[1]);

                error_msgupdate_booking = jsonObject0.getString("error_msg");
                bookingid = jsonObject0.getString("bookingid");

                error_msgupdate_participant = jsonObject1.getString("error_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Objects.equals(error_msgupdate_booking, "") && Objects.equals(error_msgupdate_participant, "")) {
                String success = "Update Success!";
                Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT);
                toast.show();
            } else if (!Objects.equals(error_msgupdate_booking, "")) {
                String OutputData = "Update booking fail : "
                        + " ," + error_msgupdate_booking;
                Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            } else if (!Objects.equals(error_msgupdate_participant, "")) {
                String OutputData = "Update participant fail : "
                        + " ," + error_msgupdate_participant;
                Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    /**
     * setOptionOnclick
     */
    private void setOptionCreate() {
        if (Objects.equals(subjectFragment.getETsubject().getText().toString(), "")) {
            Toast.makeText(this, "Not Subject", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getETdetail().getText().toString(), "")) {
            Toast.makeText(this, "Not Detail", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getDate_send(), "click to get date")) {
            Toast.makeText(this, "Date Unselected", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getRoom_spinner().getSelectedItem().toString(), "unselect")) {
            Toast.makeText(this, "Room Unselected", Toast.LENGTH_SHORT).show();
        } else if (!checkTimeInputCommon(subjectFragment.setStartTime(), subjectFragment.setEndTime())) {
            Toast.makeText(this, "Time invalid - -*", Toast.LENGTH_SHORT).show();
        } else if (accountBeen_selected_arraylist.size() == 0) {
            Toast.makeText(this, "Participant Unselected", Toast.LENGTH_SHORT).show();
        } else {
            dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            bookBean_to_create.setSubject(subjectFragment.getETsubject().getText().toString());
            bookBean_to_create.setMeeting_type(subjectFragment.getMeeting_redioButton().getText().toString());
            bookBean_to_create.setDetail(subjectFragment.getETdetail().getText().toString());
            bookBean_to_create.setDate(subjectFragment.getDate_send());
            bookBean_to_create.setStarttime(subjectFragment.setStartTime());
            bookBean_to_create.setEndtime(subjectFragment.setEndTime());
            bookBean_to_create.setRoomid(Integer.parseInt(subjectFragment.getRoom_spinner().getSelectedItem().toString())); //check
            bookBean_to_create.setProjid(Integer.parseInt(subjectFragment.getProjector_spinner().getSelectedItem().toString())); // check

            String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/dobooking";
            new doCreateBooking().execute(URL);

            Intent intent = new Intent(this, Createbooking.class);
            intent.putExtra("from", "createbook");
            startActivity(intent);
            finish();
        }


    }

    private void setOptionSave() {
        if (Objects.equals(from, "detail_to_edit")) {
            setOptionSave_Update();
        } else {
            setOptionSave_Create();
        }
    }

    private void setOptionSave_Create() {
        if (Objects.equals(subjectFragment.getETsubject().getText().toString(), "")) {
            Toast.makeText(this, "Not Subject", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getETdetail().getText().toString(), "")) {
            Toast.makeText(this, "Not Detail", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getDate_send(), "click to get date")) {
            Toast.makeText(this, "Date Unselected", Toast.LENGTH_SHORT).show();
        } else if (!checkTimeInputCommon(subjectFragment.setStartTime(), subjectFragment.setEndTime())) {
            Toast.makeText(this, "Time invalid - -*", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getRoom_spinner().getSelectedItem().toString(), "unselect")) {
            Toast.makeText(this, "Room Unselected", Toast.LENGTH_SHORT).show();
        } else if (accountBeen_selected_arraylist.size() == 0) {
            Toast.makeText(this, "Participant Unselected", Toast.LENGTH_SHORT).show();
        } else {
            dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            bookBean_to_create.setSubject(subjectFragment.getETsubject().getText().toString());
            bookBean_to_create.setMeeting_type(subjectFragment.getMeeting_redioButton().getText().toString());
            bookBean_to_create.setDetail(subjectFragment.getETdetail().getText().toString());
            bookBean_to_create.setDate(dateFormatSend.format(subjectFragment.getDate_show().getText().toString()));
            bookBean_to_create.setStarttime(subjectFragment.setStartTime());
            bookBean_to_create.setEndtime(subjectFragment.setEndTime());
            bookBean_to_create.setRoomid(Integer.parseInt(subjectFragment.getRoom_spinner().getSelectedItem().toString())); //check
            bookBean_to_create.setProjid(Integer.parseInt(subjectFragment.getProjector_spinner().getSelectedItem().toString())); // check

            String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/dobooking";
            new doCreateBooking().execute(URL);

//            bookingid = "28";
            Intent intent = new Intent(this, BookingDetail.class);
            intent.putExtra("bookingid", bookingid);
//            startActivity(intent);
//            finish();
        }
    }

    private void setOptionSave_Update() {
        Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
        if (Objects.equals(subjectFragment.getETsubject().getText().toString(), "")) {
            Toast.makeText(this, "Not Subject", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getETdetail().getText().toString(), "")) {
            Toast.makeText(this, "Not Detail", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getDate_send(), "click to get date")) {
            Toast.makeText(this, "Date Unselected", Toast.LENGTH_SHORT).show();
        } else if (!checkTimeInputCommon(subjectFragment.setStartTime(), subjectFragment.setEndTime())) {
            Toast.makeText(this, "Time invalid - -*", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(subjectFragment.getRoom_spinner().getSelectedItem().toString(), "unselect")) {
            Toast.makeText(this, "Room Unselected", Toast.LENGTH_SHORT).show();
        } else {
            Locale lc = new Locale("th", "TH");
            dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String getdate = null;
            try {
                getdate = dateFormatSend.format(dateFormatter.parse(subjectFragment.getDate_show().getText().toString()));
//                getdate = subjectFragment.getDate_show().getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            bookBean_to_create.setSubject(subjectFragment.getETsubject().getText().toString());
            bookBean_to_create.setMeeting_type(subjectFragment.getMeeting_redioButton().getText().toString());
            bookBean_to_create.setDetail(subjectFragment.getETdetail().getText().toString());
            bookBean_to_create.setDate(getdate);
            bookBean_to_create.setStarttime(subjectFragment.setStartTime());
            bookBean_to_create.setEndtime(subjectFragment.setEndTime());
            bookBean_to_create.setRoomid(Integer.parseInt(subjectFragment.getRoom_spinner().getSelectedItem().toString())); //check
            bookBean_to_create.setProjid(Integer.parseInt(subjectFragment.getProjector_spinner().getSelectedItem().toString())); // check
            bookBean_to_create.setBookingid(Integer.parseInt(bookingid));

            String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/updatebooking/restservice/updatebooking"
                    , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/updatebooking/restservice/update_participant"};
            new Update().execute(URL);

//            bookingid = "28";
            Intent intent = new Intent(this, BookingDetail.class);
            intent.putExtra("bookingid", bookingid);
//            startActivity(intent);
            finish();
        }
    }


    /**
     * other method
     */
    private void setMeeting_type(String meeting_type) {
        if (Objects.equals(meeting_type, "Problem-solving")) {
            subjectFragment.getMeeting_rediobutton_Problemsolving().setChecked(true);
        } else if (Objects.equals(meeting_type, "Brainstorming")) {
            subjectFragment.getMeeting_rediobutton_Brainstorming().setChecked(true);
        } else if (Objects.equals(meeting_type, "Training")) {
            subjectFragment.getMeeting_rediobutton_Training().setChecked(true);
        }
    }

    private boolean checkTimeInputCommon(String starttime, String endtime) {
        boolean check = true;
        SimpleDateFormat timeParse = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat timeHrformat = new SimpleDateFormat("HH", Locale.ENGLISH);
        SimpleDateFormat timeMinformat = new SimpleDateFormat("mm", Locale.ENGLISH);
        int start_hr = 0;
        int start_min = 0;
        int end_hr = 0;
        int end_min = 0;
        try {
            start_hr = Integer.parseInt(timeHrformat.format(timeParse.parse(starttime)));
            start_min = Integer.parseInt(timeMinformat.format(timeParse.parse(starttime)));
            end_hr = Integer.parseInt(timeHrformat.format(timeParse.parse(endtime)));
            end_min = Integer.parseInt(timeMinformat.format(timeParse.parse(endtime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (start_hr > end_hr) {
            check = false;
        } else if (start_hr == end_hr & start_min >= end_min) {
            check = false;
        }
        return check;
    }

    private void setTimeSpinner(String starttime, String endtime) {
        SimpleDateFormat timeParse = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat timeHrformat = new SimpleDateFormat("HH", Locale.ENGLISH);
        SimpleDateFormat timeMinformat = new SimpleDateFormat("mm", Locale.ENGLISH);
        String start_hr = null;
        String start_min = null;
        String end_hr = null;
        String end_min = null;
        try {
            start_hr = timeHrformat.format(timeParse.parse(starttime));
            start_min = timeMinformat.format(timeParse.parse(starttime));
            end_hr = timeHrformat.format(timeParse.parse(endtime));
            end_min = timeMinformat.format(timeParse.parse(endtime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Hr.
        for (int ih = 0; ih < subjectFragment.getHr().size(); ih++) {
            //start hr
            if (Objects.equals(start_hr, subjectFragment.getHr().get(ih))) {
                subjectFragment.getStarttimeHr().setSelection(ih);
            }
            //end hr
            if (Objects.equals(end_hr, subjectFragment.getHr().get(ih))) {
                subjectFragment.getEndtimeHr().setSelection(ih);
            }
        }
        //Min.
        for (int im = 0; im < subjectFragment.getMin().size(); im++) {
            //start hr
            if (Objects.equals(start_min, subjectFragment.getMin().get(im))) {
                subjectFragment.getStarttimeMin().setSelection(im);
            }
            //end hr
            if (Objects.equals(end_min, subjectFragment.getMin().get(im))) {
                subjectFragment.getEndtimeMin().setSelection(im);
            }
        }
    }

    private void setRoomSpinner(int roomSpinner) {
        for (int ir = 1; ir < roomshow_spinner_arraylist.size(); ir++) {
            if (roomSpinner == Integer.valueOf(roomshow_spinner_arraylist.get(ir))) {
                subjectFragment.getRoom_spinner().setSelection(ir);
                break;
            }
        }
    }

    private void setProjectorSpinner() {
    }


    private void clearArraylist() {
        accountBeen_selected_arraylist.clear();
        accountBeans_edit.clear();
    }


    private class SetfromSearch extends AsyncTask<String, Void, String> {
        private String date;
        private String starttime;
        private String endtime;
        private int roomid;

        private SetfromSearch(String date, String starttime, String endtime, int roomid) {
            this.date = date;
            this.starttime = starttime;
            this.endtime = endtime;
            this.roomid = roomid;
        }


        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            subjectFragment.getDate_show().setText(date);
            setTimeSpinner(starttime, endtime);
            setRoomSpinner(roomid);
        }

    }
}

