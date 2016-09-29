package com.example.wolf_z.bookingroom.Createbooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SubjectFragment extends Fragment {
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private static final String TIME_PATTERN = "HH:mm";
    private ArrayList<RoomBean> roomBeans = new ArrayList<>();
    private ArrayList<DepartmentBean> departmentBeens = new ArrayList<>();
    private ArrayList<AccountBean> accountBeens = new ArrayList<>();
    private BookBean bookBean = new BookBean();
    private ProgressDialog prgDialog;
    private Spinner timeHr;
    private Spinner timeMin;
    private Spinner totimeHr;
    private Spinner totimeMin;
    private Animation anim;
    private View view_subject;
    private View view_detail;
    protected EditText ETsubject;
    protected EditText ETdetail;
    protected RadioGroup meeting_type;
    protected RadioButton meetingButton;
    protected Spinner room;
    protected Spinner projector;
    private TextView date;
    protected ArrayList<String> Aroom = new ArrayList<>();
    protected ArrayList<String> Adepartment = new ArrayList<>();

    private String setdate;
    protected String settime;
    protected String settotime;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    protected SimpleDateFormat dateFormatSend;
    protected SimpleDateFormat dateFormatter1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_createbooking_subject, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        /** meeting_type */
        meeting_type = (RadioGroup) view.findViewById(R.id.meeting_type);
        int selectedId = meeting_type.getCheckedRadioButtonId();
        meetingButton = (RadioButton) view.findViewById(selectedId);


        /** Date picker*/
        date = (TextView) view.findViewById(R.id.date);

        Calendar caledar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

        ArrayAdapter<String> adapterhr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, hr);
        adapterhr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptermin = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, min);
        adaptermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Time
        timeHr = (Spinner) view.findViewById(R.id.timeHr);
        timeHr.setAdapter(adapterhr);
        timeMin = (Spinner) view.findViewById(R.id.timeMin);
        timeMin.setAdapter(adaptermin);
        //ToTime
        totimeHr = (Spinner) view.findViewById(R.id.totimeHr);
        totimeHr.setAdapter(adapterhr);
        totimeHr.setSelection(adapterhr.getPosition(hr.get(4)));  //set default show
        totimeMin = (Spinner) view.findViewById(R.id.totimeMin);
        totimeMin.setAdapter(adaptermin);


        /** select room */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/searchrest/restservice/getroom"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"};
        new SetData().execute(URL);
        room = (Spinner) view.findViewById(R.id.room);
        Aroom.add("unselect");
        ArrayAdapter<String> adapterroom = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Aroom);
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room.setAdapter(adapterroom);


        /**projector*/
        ArrayList<Integer> listprojectorEmpty = new ArrayList<Integer>();
        listprojectorEmpty.add(01);
        listprojectorEmpty.add(02);

        projector = (Spinner) view.findViewById(R.id.projector);
        ArrayAdapter<Integer> adapterprojector = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, listprojectorEmpty);
        adapterprojector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projector.setAdapter(adapterprojector);

        /** AnimationUtils */
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);

        view_subject = view.findViewById(R.id.view_subject);
        view_detail = view.findViewById(R.id.view_detail);

        ETsubject = (EditText) view.findViewById(R.id.ETsubject);
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

        ETdetail = (EditText) view.findViewById(R.id.ETdetail);
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


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date", date.toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        date.setText(savedInstanceState.getString("date"));
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


            Createbooking createbooking = (Createbooking) getActivity();
            createbooking.setTestcom(departmentBeens.get(1).getDepartmentPK());

        }

    }


}
