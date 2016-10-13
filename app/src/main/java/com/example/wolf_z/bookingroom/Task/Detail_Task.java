package com.example.wolf_z.bookingroom.Task;

import android.os.AsyncTask;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.Bean.Participant;
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
import java.util.HashSet;

/**
 * Created by Wolf-Z on 11/10/2559.
 */

public class Detail_Task extends AsyncTask<String, Void, String[]> {

    private String[] result = {""};
    private Participant participant;
    private ArrayList<BookBean> bookBeans = new ArrayList<>();
    private ArrayList<AccountBean> accountBeans = new ArrayList<>();

    public Detail_Task(Participant participant) {
        this.participant = participant;
    }

    @Override
    protected void onPreExecute() {
//        prgDialog.show();
    }

    protected String doCon(String url, Participant participant) {
        String result = "";
        try {
            /** POST **/
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            Gson gson = new Gson();
            StringEntity stringEntity = new StringEntity(gson.toJson(participant));
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

        result[0] = doCon(urls[0], participant);
        result[1] = doCon(urls[1], participant);

        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
//        prgDialog.dismiss();

        /** detail */
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result[0]);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BookBean bookBean = new BookBean();
                bookBean.setSubject(jsonObject.getString("subject"));
                bookBean.setMeeting_type(jsonObject.getString("meeting_type"));
                bookBean.setDate(jsonObject.getString("date"));
                bookBean.setStarttime(jsonObject.getString("starttime"));
                bookBean.setEndtime(jsonObject.getString("endtime"));
                bookBean.setDetail(jsonObject.getString("detail"));
                bookBean.setRoomid(jsonObject.getInt("roomid"));
                bookBean.setProjid(jsonObject.getInt("projid"));
                bookBeans.add(bookBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /** participant */
        try {
            jsonArray = new JSONArray(result[1]);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AccountBean accountBean = new AccountBean();
                accountBean.setDisplayname(jsonObject.getString("displayname"));
                accountBean.setDepartment(jsonObject.getString("department"));
                accountBeans.add(accountBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BookBean> getBookBeans() {
        return bookBeans;
    }

    public ArrayList<AccountBean> getAccountBeans() {
        return accountBeans;
    }
}