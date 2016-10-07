package com.example.wolf_z.bookingroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.DepartmentBean;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
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

/**
 * Created by Wolf-Z on 12/9/2559.
 */
public class RegisterActivity extends AppCompatActivity {
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    private ArrayList<DepartmentBean> departmentBeens = new ArrayList<>();
    private ArrayList<String> Adepartment = new ArrayList<>();
    private ProgressDialog prgDialog;
    private TextView errorMsg;
    private EditText displaynameET;
    private EditText usernameET;
    private EditText pwdET;
    private Spinner department;
    private AccountBean accountbean = new AccountBean();
    private Animation anim;
    private View view_name;
    private View view_email;
    private View view_password;
    private Button btnregister;
    private Button btnlogin;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        errorMsg = (TextView) findViewById(R.id.register_error);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        anim = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.scale);

        view_name = findViewById(R.id.view_name);
        view_email = findViewById(R.id.view_email);
        view_password = findViewById(R.id.view_password);

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

        /** room_spinner spinner Query */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"};
        new SetDepartment().execute(URL);

        /**department Spinner*/
        department = (Spinner) findViewById(R.id.department);
        Adepartment.add("unselect");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Adepartment);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);


        btnregister = (Button) findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check input data
                if (department.getSelectedItem() == "unselect") {
                    Toast.makeText(getApplicationContext(), "department unselected", Toast.LENGTH_LONG);
                } else {
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/registerrest/restservice/doregister";
                    /**  Params **/
                    accountbean.setDisplayname(displaynameET.getText().toString());
                    accountbean.setUsername(usernameET.getText().toString());
                    accountbean.setPassword(pwdET.getText().toString());
                    accountbean.setDepartment(department.getSelectedItem().toString());
                    new doRegister().execute(URL);
                }
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

            if (displaynameET.getText() == null || usernameET.getText() == null || pwdET.getText() == null || department.getSelectedItem().toString() == null) {
                String OutputData = " Ops! : input data is null ";
                Toast toast = Toast.makeText(getBaseContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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


    /**
     * Set Data Page
     */
    private class SetDepartment extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String result) {
            prgDialog.dismiss();
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
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

        }

    }


}
