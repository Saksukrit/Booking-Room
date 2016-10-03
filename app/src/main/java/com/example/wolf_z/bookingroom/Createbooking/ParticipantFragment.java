package com.example.wolf_z.bookingroom.Createbooking;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.DepartmentBean;
import com.example.wolf_z.bookingroom.Bean.RoomBean;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_Pname;
import com.example.wolf_z.bookingroom.Custom.CustomAdapter_Pname_select;
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
import java.util.ArrayList;


public class ParticipantFragment extends Fragment {

    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private ProgressDialog prgDialog;
    protected ListView namelist;
    protected Spinner department_type;
    protected EditText ETsearch;
    private View view_search;
    private Animation anim;
    private ArrayList<AccountBean> accountBeens = new ArrayList<>();
    private ArrayList<DepartmentBean> departmentBeens = new ArrayList<>();
    protected String[] Ldisplayname;
    protected String[] Lusername;
    protected String[] Ldepartmment;
    private Button search_participant;

    private ArrayList<String> Adepartment = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_createbooking_participant, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);


        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all"};
        new SetData().execute(URL);

        namelist = (ListView) view.findViewById(R.id.namelist);


        search_participant = (Button) view.findViewById(R.id.search_participant);
        search_participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ParticipantSearch.class);
                startActivity(intent);
            }
        });

        return view;
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
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            prgDialog.dismiss();
            JSONArray jsonArray;


            /**list name*/
            try {
                jsonArray = new JSONArray(result[0]);
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

            CustomAdapter_Pname_select adapter = new CustomAdapter_Pname_select(getActivity(), Ldisplayname, Ldepartmment, Lusername);
            namelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            namelist.setAdapter(adapter);


        }

    }

}
