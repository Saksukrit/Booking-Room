package com.example.wolf_z.bookingroom.Createbooking;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_Pname_select;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Createbooking extends AppCompatActivity {

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private static final String TIME_PATTERN = "HH:mm";
    private ArrayList<RoomBean> roomBeans = new ArrayList<>();
    private ArrayList<DepartmentBean> departmentBeens = new ArrayList<>();
    private ArrayList<AccountBean> accountBeens = new ArrayList<>();
    private ProgressDialog prgDialog;
    protected ActionBar actionBar;
    private ActionBar.Tab tabsubject, tabparticipant;
    private Fragment subjectFragment = new SubjectFragment();
    private Fragment participantFragment = new ParticipantFragment();

    private Spinner timeHr;
    private Spinner timeMin;
    private Spinner totimeHr;
    private Spinner totimeMin;
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
    private Spinner room;
    private Spinner projector;
    private TextView date;
    private ListView namelist;
    private ArrayList<String> Aroom = new ArrayList<>();
    private ArrayList<String> Adepartment = new ArrayList<>();

    private String setdate;
    private String settime;
    private String settotime;
    protected String[] Ldisplayname;
    protected String[] Lusername;
    protected String[] Ldepartmment;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatSend;
    private SimpleDateFormat dateFormatter1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createbooking);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        namelist = (ListView) findViewById(R.id.namelist);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabsubject = actionBar.newTab();
        tabsubject.setText("Subject").setTabListener(new TabListener(subjectFragment));
        actionBar.addTab(tabsubject);

        tabparticipant = actionBar.newTab();
        tabparticipant.setText("Participant").setTabListener(new TabListener(participantFragment));
        actionBar.addTab(tabparticipant);


        /** meeting_type */
        meeting_type = (RadioGroup) findViewById(R.id.meeting_type);
        int selectedId = meeting_type.getCheckedRadioButtonId();
        meetingButton = (RadioButton) findViewById(selectedId);


        /** Date picker*/
        date = (TextView) findViewById(R.id.date);

        Calendar caledar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
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
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/searchrest/restservice/getroom"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all"};
        new SetData().execute(URL);
        room = (Spinner) findViewById(R.id.room);
        Aroom.add("unselect");
        ArrayAdapter<String> adapterroom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Aroom);
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room.setAdapter(adapterroom);


        /**projector*/
        ArrayList<Integer> listprojectorEmpty = new ArrayList<Integer>();
        listprojectorEmpty.add(01);
        listprojectorEmpty.add(02);

        projector = (Spinner) findViewById(R.id.projector);
        ArrayAdapter<Integer> adapterprojector = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, listprojectorEmpty);
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projector.setAdapter(adapterprojector);


        /** department_type */
        department_type = (Spinner) findViewById(R.id.department_type);
        Adepartment.add("unselect");
        ArrayAdapter<String> adapterdepartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Adepartment);
        adapterdepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_type.setAdapter(adapterdepartment);

        /** namelist */

        String[] te = {"oo", "pp", "ii", "uu", "yy"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, te);
//        CustomAdapter_Pname adapter = new CustomAdapter_Pname(this, te, te);
//        namelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        namelist.setAdapter(adapter);

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
    }


    // setTime
    public String setTime() {
        String timehr;
        if (timeHr.getSelectedItem() == "8") {
            timehr = "08";
        } else if (timeHr.getSelectedItem() == "9") {
            timehr = "09";
        } else {
            timehr = timeHr.getSelectedItem().toString();
        }
        settime = timehr + ":" + timeMin.getSelectedItem() + ":00";
        return settime;
    }

    public String setToTime() {
        String totimehr;
        if (totimeHr.getSelectedItem() == "8") {
            totimehr = "08";
        } else if (totimeHr.getSelectedItem() == "9") {
            totimehr = "09";
        } else {
            totimehr = totimeHr.getSelectedItem().toString();
        }
        settotime = totimehr + ":" + totimeMin.getSelectedItem() + ":00";
        return settotime;
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


    /**
     * Set Data Page
     */
    private class SetData extends AsyncTask<String, Void, String[]> {

        String[] result = {};

        @Override
        protected void onPreExecute() {
            prgDialog.show();
        }

        protected String doOn(String... urls) {
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

            result[0] = doOn(urls[0]);
            result[1] = doOn(urls[1]);
            result[2] = doOn(urls[2]);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            prgDialog.dismiss();
            JSONArray jsonArray;
            /**room*/
            try {
                jsonArray = new JSONArray(result[0]);
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


            /**department*/
            try {
                jsonArray = new JSONArray(result[1]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DepartmentBean departmentBean = new DepartmentBean();
                    departmentBean.setDepartmentPK(jsonObject.getString("departmentPK"));
                    departmentBeens.add(departmentBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < departmentBeens.size(); i++) {
                Adepartment.add(String.valueOf(departmentBeens.get(i).getDepartmentPK()));
            }

            /**list name*/
            try {
                jsonArray = new JSONArray(result[2]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AccountBean accountBean = new AccountBean();
                    accountBean.setDisplayname(jsonObject.getString("displayname"));
                    accountBean.setUsername(jsonObject.getString("username"));
                    accountBean.setDepartment(jsonObject.getString("department"));
                    accountBeens.add(accountBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Ldisplayname = new String[accountBeens.size()];
            Lusername = new String[accountBeens.size()];
            Ldepartmment = new String[accountBeens.size()];
            for (int i = 0; i < accountBeens.size(); i++) {
                Ldisplayname[i] = accountBeens.get(i).getDisplayname();
                Lusername[i] = accountBeens.get(i).getUsername();
                Ldepartmment[i] = accountBeens.get(i).getDepartment();
            }


//            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, te);
//            namelist.setAdapter(adapter);
            CustomAdapter_Pname_select adapter = new CustomAdapter_Pname_select(getApplicationContext(), Ldisplayname, Ldepartmment, Lusername);
            namelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            namelist.setAdapter(adapter);

        }

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
                //condition check input null data on UI
                if (room.getSelectedItem() == "unselect") {
                    Toast.makeText(this, "Room Unselected", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(this, "Create booking", Toast.LENGTH_LONG).show();
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/dobooking";
                    /**  Params **/
                    bookBean.setSubject(ETsubject.getText().toString());
                    bookBean.setMeeting_type(meetingButton.getText().toString());
                    try {
                        bookBean.setDate(dateFormatSend.format(dateFormatter1.parse(date.getText().toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    bookBean.setStarttime(setTime());
                    bookBean.setEndtime(setToTime());
                    bookBean.setDetail(ETdetail.getText().toString());
                    bookBean.setRoomid(Integer.parseInt(room.getSelectedItem().toString()));
                    bookBean.setProjid(Integer.parseInt(projector.getSelectedItem().toString()));
                    new doCreateBooking().execute(URL);
                    Intent intent = new Intent(getApplicationContext(), MainBookingActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Create booking complete", Toast.LENGTH_LONG).show();
                    finish();
                    return true;
                }
                return false;
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


}
