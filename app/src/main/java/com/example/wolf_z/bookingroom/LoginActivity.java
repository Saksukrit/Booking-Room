package com.example.wolf_z.bookingroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
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
public class LoginActivity extends Activity {
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText usernameET;
    EditText pwdET;
    AccountBean accountBeanBean = new AccountBean();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        errorMsg = (TextView) findViewById(R.id.login_error);
        usernameET = (EditText) findViewById(R.id.loginEmail);
        pwdET = (EditText) findViewById(R.id.loginPassword);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
    }

    public void loginUser(View v) {
        String URL = "http://157.179.8.120:8080/BookingRoomService/loginrest/restservice/dologin";
        /**  Params **/
        accountBeanBean.setUsername(usernameET.getText().toString());
        accountBeanBean.setPassword(pwdET.getText().toString());
        new doLogin().execute(URL);
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
                InputStream inputStream = null;


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);

                String json = "";

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("username", accountBeanBean.getUsername());
                jsonObject.accumulate("password", accountBeanBean.getPassword());

                json = jsonObject.toString();

                StringEntity stringEntity = new StringEntity(json);

                httpPost.setEntity(stringEntity);

                //httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    result = converInputStreamToString(inputStream);
                } else {
                    result = "";
                }


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
                String success = "Login Success!";
                Toast toast = Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG);
                toast.show();
                Intent homeIntent = new Intent(getApplicationContext(), MainBookingActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                String OutputData = " Ops! : Login " + status + " "
                        + " ," + error_msg;
                Toast toast = Toast.makeText(getBaseContext(), OutputData, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void nexttoRegisterActivity(View v) {
        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registerIntent);
    }


    private String converInputStreamToString(InputStream inputStream) throws IOException {
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
