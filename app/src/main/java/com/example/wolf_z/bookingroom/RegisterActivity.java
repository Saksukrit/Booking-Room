package com.example.wolf_z.bookingroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private EditText re_pwdET;
    private Spinner department;
    private AccountBean accountbean = new AccountBean();
    private Button btnregister;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        displaynameET = (EditText) findViewById(R.id.ETdisplayname);
        usernameET = (EditText) findViewById(R.id.ETusername);
        pwdET = (EditText) findViewById(R.id.ETpassword);
        re_pwdET = (EditText) findViewById(R.id.ETre_password);
        errorMsg = (TextView) findViewById(R.id.TVerror);

        /** room_spinner spinner Query */
        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"};
        new SetDepartment().execute(URL);

        /**department Spinner*/
        department = (Spinner) findViewById(R.id.Spinnerdepartment);
        Adepartment.add("unselect");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Adepartment);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);


        btnregister = (Button) findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matcher matcher;

                //check displayname special character
                Pattern pattern_displayname = Pattern.compile("[A-Za-z. ก-ฮะ-ๅ่-้์็ฯ]", Pattern.CASE_INSENSITIVE);  // ^ as space
                boolean displayname_special_character = true;
                char[] displayname_char_check = displaynameET.getText().toString().toCharArray();
                for (int i = 0; i < displayname_char_check.length; i++) {
                    matcher = pattern_displayname.matcher(String.valueOf(displayname_char_check[i]));
                    if (!matcher.find()) {
                        displayname_special_character = false;
                        break;
                    }
                }

                //check username special character
                Pattern pattern_username = Pattern.compile("[A-Za-z0-9@. ]", Pattern.CASE_INSENSITIVE);  // ^ as space
                boolean username_special_character = true;
                char[] username_char_check = usernameET.getText().toString().toCharArray();
                for (int i = 0; i < username_char_check.length; i++) {
                    matcher = pattern_username.matcher(String.valueOf(username_char_check[i]));
                    if (!matcher.find()) {
                        username_special_character = false;
                        break;
                    }
                }

                //check password special character
                Pattern pattern_password = Pattern.compile("[A-Za-z0-9 ]", Pattern.CASE_INSENSITIVE);  // ^ as space
                boolean password_special_character = true;
                char[] pwd_char_check = pwdET.getText().toString().toCharArray();
                for (int i = 0; i < pwd_char_check.length; i++) {
                    matcher = pattern_password.matcher(String.valueOf(pwd_char_check[i]));
                    if (!matcher.find()) {
                        password_special_character = false;
                        break;
                    }
                }


                //check input data
                if (Objects.equals(displaynameET.getText().toString(), "")) {
                    Snackbar.make(v, "No Displayname", Snackbar.LENGTH_SHORT).show();
                } else if (displaynameET.length() < 2 || displaynameET.length() > 20) { //check min length
                    Snackbar.make(v, "Displayname length should be between 2 and 20", Snackbar.LENGTH_SHORT).show();
                } else if (!displayname_special_character) {
                    Snackbar.make(v, "Not accept special character in Displayname", Snackbar.LENGTH_SHORT).show();
                    //*****************************************
                } else if (Objects.equals(usernameET.getText().toString(), "")) {
                    Snackbar.make(v, "No Username", Snackbar.LENGTH_SHORT).show();
                } else if (usernameET.length() < 8 || usernameET.length() > 30) { //check min length
                    Snackbar.make(v, "Username length should be between 8 and 30", Snackbar.LENGTH_SHORT).show();
                } else if (!username_special_character) {
                    Snackbar.make(v, "Not accept special character in Username", Snackbar.LENGTH_SHORT).show();
                    //*****************************************
                } else if (Objects.equals(pwdET.getText().toString(), "")) {
                    Snackbar.make(v, "No Password", Snackbar.LENGTH_SHORT).show();
                } else if (pwdET.length() < 8 || pwdET.length() > 16) { //check min length
                    Snackbar.make(v, "Password length should be between 8 and 16", Snackbar.LENGTH_SHORT).show();
                } else if (!password_special_character) {
                    Snackbar.make(v, "Not accept special character in Password", Snackbar.LENGTH_SHORT).show();
                    //*****************************************
                } else if (!Objects.equals(re_pwdET.getText().toString(), pwdET.getText().toString())) { // Confirm Password check
                    Snackbar.make(v, "Confirm password not match", Snackbar.LENGTH_SHORT).show();
                    //*****************************************
                } else if (department.getSelectedItem() == "unselect") {
                    Snackbar.make(v, "Department unselected", Snackbar.LENGTH_SHORT).show();
                } else {
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/registerrest/restservice/doregister";
                    /**  Params **/
                    //set data
                    try {
                        accountbean.setDisplayname(URLEncoder.encode(displaynameET.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    accountbean.setUsername(usernameET.getText().toString());
                    accountbean.setPassword(pwdET.getText().toString());
                    accountbean.setDepartment(department.getSelectedItem().toString());
                    new doRegister().execute(URL);
                }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
