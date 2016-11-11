package com.example.wolf_z.bookingroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Config.Check_Internet_Connection;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
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

public class LoginActivity extends AppCompatActivity {
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private ProgressDialog prgDialog;
    private EditText usernameET;
    private EditText pwdET;
    private AccountBean accountBean = new AccountBean();
    private Animation anim;
    private View view_password;
    private View view_username;

    private Check_Internet_Connection connection = new Check_Internet_Connection(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        if (!connection.isOnline()) {
            Toast.makeText(this, "Not internet connected", Toast.LENGTH_LONG).show();
        }


        anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale);

        view_username = findViewById(R.id.second_view_username);
        view_password = findViewById(R.id.second_view_password);

        usernameET = (EditText) findViewById(R.id.edt_username);
        pwdET = (EditText) findViewById(R.id.edt_password);

        usernameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view_username.setVisibility(View.VISIBLE);
                    view_username.startAnimation(anim);
                } else {
                    view_username.setVisibility(View.GONE);
                }
            }
        });
        pwdET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    view_password.setVisibility(View.VISIBLE);
                    view_password.startAnimation(anim);
                } else {
                    view_password.setVisibility(View.GONE);
                }
            }
        });

        Button btnlogin = (Button) findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connection.isOnline()) {
                    Toast.makeText(getApplicationContext(), "Sorry ,Not internet connected", Toast.LENGTH_LONG).show();
                } else {
                    if (Objects.equals(usernameET.getText().toString(), "") || usernameET.getText().toString() == null) {
                        Snackbar.make(v, "Please enter Username ", Snackbar.LENGTH_SHORT).show();
                    } else if (Objects.equals(pwdET.getText().toString(), "") || pwdET.getText().toString() == null) {
                        Snackbar.make(v, "Please enter Password ", Snackbar.LENGTH_SHORT).show();
                    } else {
                        String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/loginrest/restservice/dologin";
                        /**  Params **/
                        accountBean.setUsername(usernameET.getText().toString());
                        accountBean.setPassword(pwdET.getText().toString());
                        new doLogin().execute(URL);
                    }
                }
            }
        });

        Button btnregister = (Button) findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });

    }

    private class doLogin extends AsyncTask<String, Void, String> {
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
                StringEntity stringEntity = new StringEntity(gson.toJson(accountBean));
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
                String success = "Login Success!";
                Toast toast = Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.putExtra("username", accountBean.getUsername());   //send intent
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            } else {
                String fail = "Login Fails ," + error_msg;
                Toast toast = Toast.makeText(getBaseContext(), fail, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

}
