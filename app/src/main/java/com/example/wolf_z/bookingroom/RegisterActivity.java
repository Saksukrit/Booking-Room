package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Wolf-Z on 12/9/2559.
 */
public class RegisterActivity extends Activity {
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText displaynameET;
    EditText usernameET;
    EditText pwdET;
    EditText departmentET;
    AccountBean accountbean = new AccountBean();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        errorMsg = (TextView) findViewById(R.id.register_error);
        displaynameET = (EditText) findViewById(R.id.registerName);
        usernameET = (EditText) findViewById(R.id.registerEmail);
        pwdET = (EditText) findViewById(R.id.registerPassword);
        departmentET = (EditText) findViewById(R.id.registerDepartment);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

    }

    public void registerUser(View v) {
        String URL = "http://157.179.8.120:8080/BookingRoomService/registerrest/restservice/doregister";
        /**  Params **/
        accountbean.setDisplayname(displaynameET.getText().toString());
        accountbean.setUsername(usernameET.getText().toString());
        accountbean.setPassword(pwdET.getText().toString());
        accountbean.setDepartment(departmentET.getText().toString());
        new doRegister().execute(URL);
    }

    private class doRegister extends AsyncTask<String, Void, String> {
        String result = "";
        private String Content;
        private String Error = null;

        @Override
        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            prgDialog.show();


        }

        @Override
        protected String doInBackground(String... urls) {


            try {
                /** POST **/
                InputStream inputStream = null;


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);

                String json = "";

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("displayname", accountbean.getDisplayname());
                jsonObject.accumulate("username", accountbean.getUsername());
                jsonObject.accumulate("password", accountbean.getPassword());
                jsonObject.accumulate("department", accountbean.getDepartment());

                json = jsonObject.toString();

                StringEntity stringEntity = new StringEntity(json);

                httpPost.setEntity(stringEntity);

                //httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                } else {
                    result = "";
                }

                Content = convertInputStreamToString(inputStream);

            } catch (JSONException e) {
                e.printStackTrace();
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

                JSONObject jsonObject = new JSONObject(result);   //result = Content. can't direct used Content

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

    public void navigatetoLoginActivity(View v) {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String resulf = "";
        while ((line = bufferedReader.readLine()) != null) {
            resulf += line;
        }
        inputStream.close();
        return resulf;
    }

}
