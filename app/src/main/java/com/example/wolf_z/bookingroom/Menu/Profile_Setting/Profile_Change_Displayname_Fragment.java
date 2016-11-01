package com.example.wolf_z.bookingroom.Menu.Profile_Setting;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;


public class Profile_Change_Displayname_Fragment extends Fragment {

    private Button btnSave_Displayname;
    private EditText edittext_new_displayname;
    private EditText edittext_password_confirm;
    private String json;
    private Profile_Setting_Activity profile_setting_activity;
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private ProgressDialog prgDialog;

    public Profile_Change_Displayname_Fragment(Profile_Setting_Activity profile_setting_activity) {
        this.profile_setting_activity = profile_setting_activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile__change__displayname_, container, false);
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        edittext_new_displayname = (EditText) view.findViewById(R.id.edittext_new_displayname);
        edittext_password_confirm = (EditText) view.findViewById(R.id.edittext_password_confirm);
        btnSave_Displayname = (Button) view.findViewById(R.id.btnSave_Displayname);
        btnSave_Displayname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(edittext_new_displayname.getText().toString(), "") || Objects.equals(edittext_new_displayname.getText().toString(), null)) {
                    Snackbar.make(view, "Not displayname", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edittext_password_confirm.getText().toString(), "") || Objects.equals(edittext_password_confirm.getText().toString(), null)) {
                    Snackbar.make(view, "Not password confirm", Snackbar.LENGTH_SHORT).show();
                } else {
                    String displayname_utf8 = "";
                    try {
                        displayname_utf8 = URLEncoder.encode(edittext_new_displayname.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    json = "{username:\"" + profile_setting_activity.getUsername() + "\",current_password:\"" + edittext_password_confirm.getText().toString() + "\",new_displayname:\"" + displayname_utf8 + "\"}";
                    //task
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/profilesetting/restservice/change_account_displayname";
                    new ChangeDisplayname_Task().execute(URL);
                }
            }
        });


        return view;
    }

    private class ChangeDisplayname_Task extends AsyncTask<String, Void, String> {
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
                StringEntity stringEntity = new StringEntity(json);
                httpPost.setEntity(stringEntity);
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.dismiss();
            String error_msg = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                error_msg = jsonObject.getString("error_msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Objects.equals(error_msg, "") || Objects.equals(error_msg, null)) {
                String success = "Change Displayname Success ";
                Toast toast = Toast.makeText(getActivity(), success, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                String OutputData = " Ops! : " + error_msg;
                Toast toast = Toast.makeText(getActivity(), OutputData, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}
