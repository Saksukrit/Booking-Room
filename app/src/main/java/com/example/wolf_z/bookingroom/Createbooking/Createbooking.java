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
import com.example.wolf_z.bookingroom.Bean.ParticipantArray;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private BookBean bookBean = new BookBean();
    private ParticipantArray participant = new ParticipantArray();

    protected SimpleDateFormat dateFormatter;
    protected SimpleDateFormat dateFormatSend;
    protected SimpleDateFormat dateFormatter1;


    public static ArrayList<AccountBean> accountBeen_selected_arraylist = new ArrayList<>();
    private ArrayList<String> roomshow_spinner_arraylist = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createbooking);
        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        actionBar = getSupportActionBar();

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
                if (Objects.equals(subjectFragment.getETsubject().getText().toString(), "")) {
                    Toast.makeText(this, "Not Subject", Toast.LENGTH_SHORT).show();
                } else if (Objects.equals(subjectFragment.getETdetail().getText().toString(), "")) {
                    Toast.makeText(this, "Not Detail", Toast.LENGTH_SHORT).show();
                } else if (Objects.equals(subjectFragment.getSetdate(), "click to get date")) {
                    Toast.makeText(this, "Date Unselected", Toast.LENGTH_SHORT).show();
                } else if (Objects.equals(subjectFragment.getRoom_spinner().getSelectedItem().toString(), "unselect")) {
                    Toast.makeText(this, "Room Unselected", Toast.LENGTH_SHORT).show();
                } else {
                    bookBean.setSubject(subjectFragment.getETsubject().getText().toString());
                    bookBean.setMeeting_type(subjectFragment.getMeeting_redioButton().getText().toString());
                    bookBean.setDetail(subjectFragment.getETdetail().getText().toString());
                    bookBean.setDate(subjectFragment.getSetdate());
                    bookBean.setStarttime(subjectFragment.setStartTime());
                    bookBean.setEndtime(subjectFragment.setEndTime());
                    bookBean.setRoomid(Integer.parseInt(subjectFragment.getRoom_spinner().getSelectedItem().toString())); //check
                    bookBean.setProjid(Integer.parseInt(subjectFragment.getProjector_spinner().getSelectedItem().toString())); // check

                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/dobooking";
                    new doCreateBooking().execute(URL);
                }
                return true;
            case android.R.id.home:
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

    public void setAccountBeen_selected(ArrayList<AccountBean> accountBeen_selected) {
        this.accountBeen_selected_arraylist = accountBeen_selected;
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
        protected void onPreExecute() {
            //Start Progress Dialog (Message)
//            prgDialog.show();
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
//            prgDialog.dismiss();

            String tag = "";
            String status = "";
            String error_msg = "";
            String bookingid = "";
            try {

                JSONObject jsonObject = new JSONObject(result);

                tag = jsonObject.getString("tag");
                status = jsonObject.getString("status");
                error_msg = jsonObject.getString("error_msg");
                bookingid = jsonObject.getString("bookingid");   //***********


                String[] username = new String[accountBeen_selected_arraylist.size()];
                for (int i = 0; i < accountBeen_selected_arraylist.size(); i++) {
                    username[i] = accountBeen_selected_arraylist.get(i).getUsername();
                }


                /**Params participant*/
                participant.setBookingid(Integer.parseInt(bookingid));
                participant.setUsername(username);
                String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/doparticipant";
                new doCreateParticipant().execute(URL);


            } catch (JSONException e) {

                e.printStackTrace();
            }

            if (Objects.equals(error_msg, "")) {
                String success = "Create Book Success! ";
                Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                String OutputData = " Ops! : Booking " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    /**
     * Create Participant
     */
    private class doCreateParticipant extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                Gson gson = new Gson();

                String x = gson.toJson(participant);
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
//            prgDialog.dismiss();

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
                String success = "Add Participant Success!";
                Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                String OutputData = " Ops! : Participant " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getApplicationContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }


    /**
     * Set Data Page
     */
    private class SetData extends AsyncTask<String, Void, String[]> {

        String[] result = {};

        @Override
        protected void onPreExecute() {
//            prgDialog.show();
        }

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
//            prgDialog.dismiss();
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

}
