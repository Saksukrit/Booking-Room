package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Wolf-Z on 12/9/2559.
 */
public class RegisterActivity extends Activity {
    private ProgressDialog prgDialog;
    private TextView errorMsg;
    private EditText displaynameET;
    private EditText usernameET;
    private EditText pwdET;
    private EditText departmentET;
    private AccountBean accountbean = new AccountBean();
    private Animation anim;
    private View view_name;
    private View view_email;
    private View view_password;
    private View view_department;
    private Button btnregister;
    private Button btnlogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        errorMsg = (TextView) findViewById(R.id.register_error);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        anim = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.scale);

        view_name = findViewById(R.id.view_name);
        view_email = findViewById(R.id.view_email);
        view_password = findViewById(R.id.view_password);
        view_department = findViewById(R.id.view_deparment);

        displaynameET = (EditText) findViewById(R.id.registerName);
        displaynameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_name.setVisibility(View.VISIBLE);
                    view_name.startAnimation(anim);
                } else {
                    view_name.setVisibility(View.GONE);
                }
            }
        });

        usernameET = (EditText) findViewById(R.id.registerEmail);
        usernameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_email.setVisibility(View.VISIBLE);
                    view_email.startAnimation(anim);
                } else {
                    view_email.setVisibility(View.GONE);
                }
            }
        });

        pwdET = (EditText) findViewById(R.id.registerPassword);
        pwdET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_password.setVisibility(View.VISIBLE);
                    view_password.startAnimation(anim);
                } else {
                    view_password.setVisibility(View.GONE);
                }
            }
        });

        departmentET = (EditText) findViewById(R.id.registerDepartment);
        departmentET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view_department.setVisibility(View.VISIBLE);
                    view_department.startAnimation(anim);
                } else {
                    view_department.setVisibility(View.GONE);
                }
            }
        });

        btnregister = (Button) findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://157.179.8.120:8080/BookingRoomService/registerrest/restservice/doregister";
                /**  Params **/
                accountbean.setDisplayname(displaynameET.getText().toString());
                accountbean.setUsername(usernameET.getText().toString());
                accountbean.setPassword(pwdET.getText().toString());
                accountbean.setDepartment(departmentET.getText().toString());
                new doRegister().execute(URL);
            }
        });

        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });


    }


    private class doRegister extends AsyncTask<String, Void, String> {
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
                StringEntity stringEntity = new StringEntity(gson.toJson(accountbean));

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
                String success = "Register Success!";
                Toast toast = Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                // Clears History of Activity
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            } else {
                String OutputData = " Ops! : Register " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getBaseContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

}
