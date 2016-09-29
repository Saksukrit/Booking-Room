package com.example.wolf_z.bookingroom.Createbooking;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
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
    protected String[] Ldisplayname;
    protected String[] Lusername;
    protected String[] Ldepartmment;

    private ArrayList<String> Adepartment = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_createbooking_participant, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);


        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all"};

        namelist = (ListView) view.findViewById(R.id.namelist);
//        String[] te = {"oo", "pp", "ii", "uu", "yy"};
//        CustomAdapter_Pname adapter = new CustomAdapter_Pname(getActivity(), te, te);
//        namelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        namelist.setAdapter(adapter);

        /** department_type */
        department_type = (Spinner) view.findViewById(R.id.department_type);
        Adepartment.add("unselect");
        ArrayAdapter<String> adapterdepartment = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Adepartment);
        adapterdepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_type.setAdapter(adapterdepartment);


        /** AnimationUtils */
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);

        view_search = view.findViewById(R.id.view_search);
        ETsearch = (EditText) view.findViewById(R.id.ETsearch);
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

            CustomAdapter_Pname_select adapter = new CustomAdapter_Pname_select(getActivity(), Ldisplayname, Ldepartmment, Lusername);
            namelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            namelist.setAdapter(adapter);


        }

    }

}
