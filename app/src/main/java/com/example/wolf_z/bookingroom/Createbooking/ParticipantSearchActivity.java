package com.example.wolf_z.bookingroom.Createbooking;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.wolf_z.bookingroom.Config.ServiceURLconfig;
import com.example.wolf_z.bookingroom.Createbooking.Multi_select_iin_Search.MultiSelectRecyclerViewAdapter;
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

public class ParticipantSearchActivity extends AppCompatActivity implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {
    private ServiceURLconfig serviceURLconfig = new ServiceURLconfig();
    protected ActionBar actionBar;
    private Toolbar toolbar;
    protected ProgressDialog prgDialog;
    protected ArrayList<String> display_auto = new ArrayList<>();

    private ArrayList<DepartmentBean> departmentBeens_arraylist = new ArrayList<>();
    protected Spinner department_type_spinner;
    private ArrayList<String> department_arraylist = new ArrayList<>();

    private AccountBean param_send_service = new AccountBean();
    protected RecyclerView select_recyclerview;
    private MultiSelectRecyclerViewAdapter item_Adapter;
    protected Button search_button;
    protected Button undo_button;
    private AutoCompleteTextView auto_search;
    private View snack_view;
    private ArrayList<AccountBean> accountBeens_arraylist = new ArrayList<>();
    private ArrayList<AccountBean> account_item_selected_arraylist = new ArrayList<>();
    private boolean searchstatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_participant);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBar = getSupportActionBar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        snack_view = findViewById(R.id.snack_view);


        String[] URL = {serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/getdepartment"
                , serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all_autocomplete"};
        new SetDepartment().execute(URL);

        /** auto_search click item to search_button list*/
        final ArrayAdapter<String> auto_adapter = new ArrayAdapter<>(this
                , android.R.layout.simple_dropdown_item_1line, display_auto);

        auto_search = (AutoCompleteTextView) findViewById(R.id.auto_search);
        auto_search.setThreshold(3);//set minimum char
        auto_search.setAdapter(auto_adapter);
        Auto_setOnClickListener();

        /** select_recyclerview **RecyclerView*/
        select_recyclerview = (RecyclerView) findViewById(R.id.list_participant);
        select_recyclerview.setHasFixedSize(true);
        select_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        /** department_type_spinner */
        department_type_spinner = (Spinner) findViewById(R.id.department_type);
        department_arraylist.add("all");
        ArrayAdapter<String> adapterdepartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, department_arraylist);
        adapterdepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_type_spinner.setAdapter(adapterdepartment);

        /**Button Search*/
        search_button = (Button) findViewById(R.id.search);
        Search_setOnClickListener();

        undo_button = (Button) findViewById(R.id.undo);
        Undo_setOnClickListener();

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
        item_Adapter.toggleSelection(position);
    }

    /**
     * Action Bar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 0:
                if (!searchstatus) {
                    Snackbar.make(snack_view, "You not search", Snackbar.LENGTH_SHORT).show();
                } else if (item_Adapter.getSelectedItemCount() != 0) {
                    /**loop get data by position mapping index*/
                    for (int i = 0; i < item_Adapter.getItemCount(); i++) {
                        for (int j = 0; j < item_Adapter.getSelectedItemCount(); j++) {
                            if (i == item_Adapter.getSelectedItems().get(j)) {
                                account_item_selected_arraylist.add(accountBeens_arraylist.get(i));
                            }
                        }
                    }
//                    Createbooking.accountBeen_selected_arraylist.addAll(new ArrayList<>(account_item_selected_arraylist));


                    if (Createbooking.accountBeen_selected_arraylist.size() != 0) {
                        for (int i = 0; i < Createbooking.accountBeen_selected_arraylist.size(); i++) {
                            for (int j = i + 1; j < account_item_selected_arraylist.size(); j++) {
                                if (i != j && !Objects.equals(account_item_selected_arraylist.get(j).getUsername(), Createbooking.accountBeen_selected_arraylist.get(i).getUsername())) {
                                    Createbooking.accountBeen_selected_arraylist.add(account_item_selected_arraylist.get(j));
                                }
                            }
                        }
                    } else {
                        Createbooking.accountBeen_selected_arraylist.addAll(new ArrayList<>(account_item_selected_arraylist));

                    }

//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList("detailBeanList", (ArrayList<? extends Parcelable>) accountBeen_selected);
//                    intent.putExtra("bundle", bundle);

                    int resultCode = RESULT_OK;
                    Intent intent = new Intent();
                    setResult(resultCode, intent);

                    account_item_selected_arraylist.clear();
                    finish();
                } else {
                    Snackbar.make(snack_view, "Not participant selected", Snackbar.LENGTH_SHORT).show();
                }


                return true;
            case 1:
                accountBeens_arraylist.clear();
                item_Adapter = new MultiSelectRecyclerViewAdapter(ParticipantSearchActivity.this, accountBeens_arraylist, ParticipantSearchActivity.this);
                select_recyclerview.setAdapter(item_Adapter);
                auto_search.setText("");
                department_type_spinner.setSelection(0);
                Toast.makeText(getApplicationContext(), "clear", Toast.LENGTH_SHORT).show();
                return true;
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
            mnu2.setIcon(R.drawable.clearicon23);
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
                    departmentBeens_arraylist.add(departmentBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < departmentBeens_arraylist.size(); i++) {
                department_arraylist.add(String.valueOf(departmentBeens_arraylist.get(i).getDepartmentPK()));
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

            departmentBeens_arraylist.clear();
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
                StringEntity stringEntity = new StringEntity(gson.toJson(param_send_service));  //
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
                    accountBeens_arraylist.add(accountBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (accountBeens_arraylist.size() == 0) {
                Toast.makeText(getApplication(), "No Data", Toast.LENGTH_SHORT).show();
            }

            item_Adapter = new MultiSelectRecyclerViewAdapter(ParticipantSearchActivity.this, accountBeens_arraylist, ParticipantSearchActivity.this);
            select_recyclerview.setAdapter(item_Adapter);

        }

    }


    /**
     * get Event each view
     */
    public void Search_setOnClickListener() {
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountBeens_arraylist.clear();
                searchstatus = true;
                String URL = null;
                if (Objects.equals(auto_search.getText().toString(), "") && department_type_spinner.getSelectedItem() == "all") {
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_all";
                    new Searchlist().execute(URL);

                } else if (!Objects.equals(auto_search.getText().toString(), "") && department_type_spinner.getSelectedItem() == "all") {
                    param_send_service.setDisplayname(auto_search.getText().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name";
                    new Searchlist().execute(URL);

                } else if (!Objects.equals(auto_search.getText().toString(), "") && department_type_spinner.getSelectedItem() != "all") {
                    param_send_service.setDisplayname(auto_search.getText().toString());
                    param_send_service.setDepartment(department_type_spinner.getSelectedItem().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name_department";
                    new Searchlist().execute(URL);

                } else if (Objects.equals(auto_search.getText().toString(), "") && department_type_spinner.getSelectedItem() != "all") {
                    param_send_service.setDepartment(department_type_spinner.getSelectedItem().toString());
                    URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_department";
                    new Searchlist().execute(URL);

                } else {
                    Toast.makeText(getApplication(), "can't search_button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Auto_setOnClickListener() {
        auto_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountBeens_arraylist.clear();
                searchstatus = true;
                if (department_type_spinner.getSelectedItem().toString() == "all") {
                    param_send_service.setDisplayname(auto_search.getText().toString());
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name";
                    new Searchlist().execute(URL);
                } else {
                    param_send_service.setDisplayname(auto_search.getText().toString());
                    param_send_service.setDepartment(department_type_spinner.getSelectedItem().toString());
                    String URL = serviceURLconfig.getLocalhosturl() + "/BookingRoomService/bookingrest/restservice/account_by_name_department";
                    new Searchlist().execute(URL);
                }

            }
        });
    }

    public void Undo_setOnClickListener() {
        undo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (item_Adapter.getItemCount() != 0) {
                        item_Adapter.clearSelection();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
