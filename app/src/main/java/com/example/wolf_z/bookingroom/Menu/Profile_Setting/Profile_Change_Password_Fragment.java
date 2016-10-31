package com.example.wolf_z.bookingroom.Menu.Profile_Setting;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.wolf_z.bookingroom.MainBookingActivity;
import com.example.wolf_z.bookingroom.R;
import com.google.gson.Gson;

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
import java.util.Objects;


public class Profile_Change_Password_Fragment extends Fragment {

    protected Button btnSave_Password;
    private EditText edit_current_password;
    private EditText edit_new_password;
    private EditText edit_new_again_password;
    private ProgressDialog prgDialog;
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private String json;
    Profile_Setting_Activity profile_setting_activity;

    public Profile_Change_Password_Fragment(Profile_Setting_Activity profile_setting_activity) {
        this.profile_setting_activity = profile_setting_activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile__change__password_, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        edit_current_password = (EditText) view.findViewById(R.id.edit_current_password);
        edit_new_password = (EditText) view.findViewById(R.id.edit_new_password);
        edit_new_again_password = (EditText) view.findViewById(R.id.edit_new_again_password);
        btnSave_Password = (Button) view.findViewById(R.id.btnSave_Password);
        btnSave_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(edit_current_password.getText().toString(), "") || Objects.equals(edit_current_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not current password", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edit_new_password.getText().toString(), "") || Objects.equals(edit_new_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not new password", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edit_new_again_password.getText().toString(), "") || Objects.equals(edit_new_again_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not again password", Snackbar.LENGTH_SHORT).show();
                } else if (!Objects.equals(edit_new_again_password.getText().toString(), edit_new_password.getText().toString())) {
                    Snackbar.make(view, "Invalid again password", Snackbar.LENGTH_SHORT).show();
                } else {
                    json = "{username:\"" + profile_setting_activity.getUsername() + "\",current_password:\"" + edit_current_password.getText().toString() + "\",new_password:\"" + edit_new_password.getText().toString() + "\"}";
                    // task
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/profilesetting/restservice/change_account_password";
                    new ChangePassword_Task().execute(URL);
                }
            }
        });

        return view;
    }


    private class ChangePassword_Task extends AsyncTask<String, Void, String> {
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
                String success = "Change Password Success ";
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
