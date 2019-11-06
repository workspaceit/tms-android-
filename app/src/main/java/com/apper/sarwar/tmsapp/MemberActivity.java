package com.apper.sarwar.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.apper.sarwar.tmsapp.Adapter.AutoSuggestAdapter;
import com.apper.sarwar.tmsapp.apiclients.auth.ApiCall;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthClient;
import com.apper.sarwar.tmsapp.apiclients.auth.MemberClient;
import com.apper.sarwar.tmsapp.apiclients.auth.MemberRequestListener;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.apper.sarwar.tmsapp.entity.MemberModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MemberActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MemberRequestListener {

    NavigationView navigationView;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<MemberModel> memberModels;
    private MemberModel memberModel;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    LinkedHashMap<String, String> lH = new LinkedHashMap<String, String>();

    Button add_member_button;
    EditText selectedMemberId;
    String urlAuthorization, orgName;

    MemberClient memberClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final AppCompatAutoCompleteTextView autoCompleteTextView =
                findViewById(R.id.member_list_member_complete_text_view);
        final TextView selectedText = findViewById(R.id.selected_item);
        selectedMemberId = findViewById(R.id.member_id);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        System.out.println(lH.keySet());
                        selectedText.setText(autoSuggestAdapter.getObject(position));
                        String memberId = (new ArrayList<String>(lH.keySet())).get(position);
                        selectedMemberId.setText(memberId);
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        memberClient = new MemberClient(this);


        orgName = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgId, this);
        urlAuthorization = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.urlAuthorization, this);


        add_member_button = (Button) findViewById(R.id.add_member_button);
        add_member_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMemberId = (EditText) findViewById(R.id.member_id);
                String selected_member_id = selectedMemberId.getText().toString().trim();
//                memberClient.addMember(orgName, selected_member_id, urlAuthorization);
                addMember(orgName, selected_member_id, urlAuthorization);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name_singed);
        String userId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userId, this);
        navUsername.setText(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userName, this));


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem app_bar_switch_organization = menu.findItem(R.id.app_bar_switch_organization);
        app_bar_switch_organization.setTitle(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName, getApplicationContext()));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addMember(String orgName, String selected_member_id, String urlAuthorization) {
        LoadingMessageUtil.startLoadingDialog(this, "Processing..");
        memberClient.addMember(orgName, selected_member_id, urlAuthorization);
    }


    private void makeApiCall(String text) {

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
/*
                        stringList.add(row.getString("name"));
*/
                        stringList.add(row.getString("name"));
                        lH.put(row.getString("uid"), row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.create_organization) {
            Intent intent = new Intent(this, OrganizationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.create_project) {
            Intent intentProject = new Intent(this, ProjectActivity.class);
            startActivity(intentProject);

        } else if (id == R.id.add_member) {
            Intent intentProject = new Intent(this, MemberActivity.class);
            startActivity(intentProject);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMemberAddedSuccess(JSONObject jsonObject) {
        try {

            LoadingMessageUtil.endLoadingDialog();
            /*String orgSlug = (String) jsonObject.get("slug");*/
            System.out.println(jsonObject);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Member added successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMemberAddedFailure() {
        try {

            LoadingMessageUtil.endLoadingDialog();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
