package com.example.wolf_z.bookingroom.Createbooking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wolf_z.bookingroom.Bean.AccountBean;
import com.example.wolf_z.bookingroom.Bean.DepartmentBean;
import com.example.wolf_z.bookingroom.Config.KeyboardManager;
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Createbooking.Multi_Search.MultiSelectRecyclerViewAdapter;
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
import java.util.Objects;

public class ParticipantSearch extends AppCompatActivity implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    protected ActionBar actionBar;
    private Toolbar toolbar;
    protected ProgressDialog prgDialog;
    protected ArrayList<String> list_participant = new ArrayList<>();
    protected ArrayList<String> display_auto = new ArrayList<>();

    private ArrayList<DepartmentBean> departmentBeens = new ArrayList<>();
    protected Spinner department_type;
    private ArrayList<String> Adepartment = new ArrayList<>();
    private ArrayList<AccountBean> accountBeens = new ArrayList<>();
    private AccountBean param_send = new AccountBean();
    protected RecyclerView select_list;
    private MultiSelectRecyclerViewAdapter mAdapter;
    protected Button search;
    protected Button undo;
    private AutoCompleteTextView auto_search;
    private View view;
    private Snackbar snackbar_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_participant);
        KeyboardManager.on(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBar = getSupportActionBar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("MultiSelectRecylcerView");

        }
        view = findViewById(R.id.snack_view);


        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all_autocomplete"};
        new SetDepartment().execute(URL);

        /** auto_search click item to search list*/
        final ArrayAdapter<String> auto_adapter = new ArrayAdapter<>(this
                , android.R.layout.simple_dropdown_item_1line, display_auto);

        auto_search = (AutoCompleteTextView) findViewById(R.id.auto_search);
        auto_search.setThreshold(3);//set minimum char
        auto_search.setAdapter(auto_adapter);
        auto_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountBeens.clear();
                if (department_type.getSelectedItem().toString() == "all") {
                    param_send.setDisplayname(auto_search.getText().toString());
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name";
                    new Searchlist().execute(URL);
                } else {
                    param_send.setDisplayname(auto_search.getText().toString());
                    param_send.setDepartment(department_type.getSelectedItem().toString());
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name_department";
                    new Searchlist().execute(URL);
                }

            }
        });

        select_list = (RecyclerView) findViewById(R.id.list_participant);
        select_list.setHasFixedSize(true);
        select_list.setLayoutManager(new LinearLayoutManager(this));

        /** department_type */
        department_type = (Spinner) findViewById(R.id.department_type);
        Adepartment.add("all");
        ArrayAdapter<String> adapterdepartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Adepartment);
        adapterdepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_type.setAdapter(adapterdepartment);

        /**Button Search*/
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountBeens.clear();
                String URL = null;
                if (Objects.equals(auto_search.getText().toString(), "") && department_type.getSelectedItem() == "all") {
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all";
                    new Searchlist().execute(URL);

                } else if (!Objects.equals(auto_search.getText().toString(), "") && department_type.getSelectedItem() == "all") {
                    param_send.setDisplayname(auto_search.getText().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name";
                    new Searchlist().execute(URL);

                } else if (!Objects.equals(auto_search.getText().toString(), "") && department_type.getSelectedItem() != "all") {
                    param_send.setDisplayname(auto_search.getText().toString());
                    param_send.setDepartment(department_type.getSelectedItem().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name_department";
                    new Searchlist().execute(URL);

                } else if (Objects.equals(auto_search.getText().toString(), "") && department_type.getSelectedItem() != "all") {
                    param_send.setDepartment(department_type.getSelectedItem().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_department";
                    new Searchlist().execute(URL);

                } else {
                    Toast.makeText(getApplication(), "can't search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        undo = (Button) findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mAdapter.getItemCount() != 0) {
                        mAdapter.clearSelection();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void checklistonclick() {
//        snackbar_selected.show();
        Snackbar.make(view, "Item Selected", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }


    @Override
    public void onItemClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                String s = "";
                /**loop get data by position mapping index*/
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    for (int j = 0; j < mAdapter.getSelectedItemCount(); j++) {
                        if (i == mAdapter.getSelectedItems().get(j)) {
                            s += accountBeens.get(i).getDisplayname() + "  ";
                        }
                    }
                }
                Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
                return true;
            case 1:
                accountBeens.clear();
                mAdapter = new MultiSelectRecyclerViewAdapter(ParticipantSearch.this, accountBeens, ParticipantSearch.this);
                select_list.setAdapter(mAdapter);
                auto_search.setText("");
                department_type.setSelection(0);
                Toast.makeText(getApplicationContext(), "clear", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem mnu1 = menu.add(0, 0, 0, "add");
        {
            mnu1.setIcon(R.drawable.addicon2);
            mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT); //instance with snack bar
        }

        MenuItem mnu2 = menu.add(1, 1, 1, "clear");
        {
            mnu2.setIcon(R.drawable.clearicon);
            mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return true;
    }

    /**
     * Set Department and Name-Autocomplete
     */
    private class SetDepartment extends AsyncTask<String, Void, String[]> {

        String[] result = {};

        @Override
        protected void onPreExecute() {
//            prgDialog.show();
        }

        String doOn(String... urls) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected String[] doInBackground(String... urls) {
            result = new String[urls.length];
            result[0] = doOn(urls[0]);
            result[1] = doOn(urls[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            prgDialog.dismiss();
            JSONArray jsonArray;
            /**department*/
            try {
                jsonArray = new JSONArray(result[0]);
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


            /**list name autocomplete*/
            try {
                jsonArray = new JSONArray(result[1]);
                String[] check = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    display_auto.add(jsonObject.getString("displayname"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (display_auto.size() == 0) {
                Toast.makeText(getApplication(), "No Data", Toast.LENGTH_LONG).show();
            }

            departmentBeens.clear();
        }

    }


    /**
     * Search list
     */
    private class Searchlist extends AsyncTask<String, Void, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
//            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                /** POST **/
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                Gson gson = new Gson();
                StringEntity stringEntity = new StringEntity(gson.toJson(param_send));  //
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
//            prgDialog.dismiss();
            JSONArray jsonArray;

            /**list name*/
            try {
                jsonArray = new JSONArray(result);
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
            if (accountBeens.size() == 0) {
                Toast.makeText(getApplication(), "No Data", Toast.LENGTH_SHORT).show();
            }

            mAdapter = new MultiSelectRecyclerViewAdapter(ParticipantSearch.this, accountBeens, ParticipantSearch.this);
            select_list.setAdapter(mAdapter);

        }

    }
}
