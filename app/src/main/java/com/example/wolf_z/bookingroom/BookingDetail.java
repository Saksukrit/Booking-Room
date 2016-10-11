package com.example.wolf_z.bookingroom;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.Participant;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_Pname;
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
import java.util.ArrayList;

public class BookingDetail extends AppCompatActivity {

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    protected Bundle bundle;
    protected String username;
    protected String bookingid;
    private ArrayList<BookBean> bookBeans = new ArrayList<>();
    private ArrayList<AccountBean> accountBeans = new ArrayList<>();
    private Participant participant = new Participant();
    private ProgressDialog prgDialog;
    protected ActionBar actionBar;
    private TextView txtsubject;
    private TextView txtdetail;
    private TextView txtmeetingtype;
    private TextView txtdate;
    private TextView txtstarttime;
    private TextView txtendtime;
    private TextView txtroomid;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        bookingid = bundle.getString("bookingid");


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        listView = (ListView) findViewById(R.id.Pnamelist);
        txtsubject = (TextView) findViewById(R.id.subject);
        txtdetail = (TextView) findViewById(R.id.detail);
        txtmeetingtype = (TextView) findViewById(R.id.meetingtype);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txtstarttime = (TextView) findViewById(R.id.txtstarttime);
        txtendtime = (TextView) findViewById(R.id.txtendtime);
        txtroomid = (TextView) findViewById(R.id.roomid);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /** Query */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/showdetail"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/mainrest/restservice/showpaticipant"};
        //  Params
        participant.setUsername(username);
        participant.setBookingid(Integer.parseInt(bookingid));
        new Detail().execute(URL);

        Toast.makeText(getApplicationContext(), username + "  " + bookingid, Toast.LENGTH_LONG).show();


    }


    private class Detail extends AsyncTask<String, Void, String[]> {

        String[] result = {""};

        @Override
        protected void onPreExecute() {
            prgDialog.show();
        }

        protected String doCon(String url, Participant participant) {
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

            result[0] = doCon(urls[0], participant);
            result[1] = doCon(urls[1], participant);

            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            prgDialog.dismiss();

            /** detail */
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
                    bookBeans.add(bookBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < bookBeans.size(); i++) {
                txtsubject.setText(bookBeans.get(i).getSubject());
                txtdetail.setText(bookBeans.get(i).getDetail());
                txtmeetingtype.setText(bookBeans.get(i).getMeeting_type());
                txtdate.setText(bookBeans.get(i).getDate());
                txtstarttime.setText(bookBeans.get(i).getStarttime());
                txtendtime.setText(bookBeans.get(i).getEndtime());
                txtroomid.setText(String.valueOf(bookBeans.get(i).getRoomid()));
            }
            /**********************************************************/

            /** participant */
            try {
                jsonArray = new JSONArray(result[1]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    AccountBean accountBean = new AccountBean();
                    accountBean.setDisplayname(jsonObject.getString("displayname"));
                    accountBean.setDepartment(jsonObject.getString("department"));
                    accountBeans.add(accountBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomAdapter_Pname adapter = new CustomAdapter_Pname(getApplicationContext(), accountBeans);
            listView.setAdapter(adapter);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem mnu1 = menu.add(0, 0, 0, "Edit");
        {
            mnu1.setIcon(R.drawable.edit128);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "Go Edit", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}
