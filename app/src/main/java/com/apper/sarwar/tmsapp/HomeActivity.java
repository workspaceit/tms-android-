package com.apper.sarwar.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.Adapter.MyTaskListAdapter;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthClient;
import com.apper.sarwar.tmsapp.apiclients.auth.OrganizationClient;
import com.apper.sarwar.tmsapp.apiclients.auth.OrganizationListListener;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.apper.sarwar.tmsapp.viewmodel.MyTaskList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OrganizationListListener {

    NavigationView navigationView;
    AuthClient authClient;
    String urlAuthorization;
    ImageView nav_settings;
    Switch onOffSwitch;
    Menu menu;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<MyTaskList> myTaskListModel;
    private static final String TAG = "HomeActivity";
    public String authorizationToken, orgSlug, orgId;
    private OrganizationClient organizationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Are you want to create a new task?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/*
        DrawerUtil.getDrawer(this, toolbar);
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name_singed);
        TextView navOrgName = (TextView) headerView.findViewById(R.id.header_organization);


        String userId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userId, this);
        orgId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgId, this);
        navUsername.setText(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userName, this));
        navOrgName.setText(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName, this));


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        MenuItem app_bar_switch_organization = menu.findItem(R.id.app_bar_switch_organization);
        app_bar_switch_organization.setTitle(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName, this.getApplicationContext()));

        authorizationToken = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.urlAuthorization, this);
        orgSlug = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgSlug, this);
        organizationClient = new OrganizationClient(this);

        organizationClient.getAllOrganization(authorizationToken);
        organizationClient.getAllProjectByOrg(authorizationToken, orgSlug);
/*
        organizationClient.getMemberByOrg(authorizationToken, orgId);
*/


        try {
            onOffSwitch = (Switch) headerView.findViewById(R.id.nav_switch_button);
            onOffSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        navigationView.getMenu().setGroupVisible(R.id.nav_main_section, false);
                        navigationView.getMenu().setGroupVisible(R.id.nav_settings_section, true);
                    } else {
                        navigationView.getMenu().setGroupVisible(R.id.nav_main_section, true);
                        navigationView.getMenu().setGroupVisible(R.id.nav_settings_section, false);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        myTaskListModel = new ArrayList<>();

        try {

            for (int i = 1; i <= 15; i++) {

                System.out.println("Increment! " + i);

                MyTaskList myList = new MyTaskList(
                        "task_" + i,
                        "Dummy Heading " + i,
                        "Lorem."
                );
                System.out.println(myList);
                System.out.println("Hello World!");
                myTaskListModel.add(myList);
            }

            recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new MyTaskListAdapter(myTaskListModel, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        } else if (id == R.id.nav_logout) {
            LoadingMessageUtil.startLoadingDialog(this, "Log outing...");
            SharedPreferenceUtil.logOut(this);
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getGroupId() == 255) {
            Toast.makeText(this, item.getTitleCondensed(), Toast.LENGTH_SHORT).show();
        } else if (item.getGroupId() == 256) {
            String projectId = item.getTitleCondensed().toString();
            organizationClient.getAllTaskByProject(authorizationToken, orgSlug, projectId);
            SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.projectId, projectId, this);

        } else if (id == R.id.nav_my_settings) {

            Toast.makeText(this, item.getTitleCondensed(), Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onOrganizationListSuccess(JSONObject jsonObject) {

        try {

            JSONArray orgList = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < orgList.length(); i++) {
                JSONObject row = orgList.getJSONObject(i);
                String id = (String) row.get("id");
                String orgName = (String) row.get("name");
                String slugName = (String) row.get("slug");
                String idSlug = id + "-" + slugName;
                System.out.println(id);
                MenuItem nav_organization_item = menu.findItem(R.id.nav_organization);
                SubMenu subMenu = nav_organization_item.getSubMenu();
                String selectedId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgId, this);
                subMenu.add(255, i, i, orgName).setTitleCondensed(idSlug);
            }

            Log.d(TAG, "onOrgListFailed: ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onOrganizationListFailure() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "You do not have organization yet!", Toast.LENGTH_LONG).show();
            }
        });

        Log.d(TAG, "onOrgListSuccess");
    }

    @Override
    public void onProjectListSuccess(JSONObject jsonObject) {
        try {
            JSONArray projectList = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < projectList.length(); i++) {
                JSONObject row = projectList.getJSONObject(i);
                String projectName = (String) row.get("name");
                String projectId = (String) row.get("id");
                MenuItem nav_organization_item = menu.findItem(R.id.project_item_section);
                SubMenu subMenu = nav_organization_item.getSubMenu();
                subMenu.add(256, i, i, projectName).setTitleCondensed(projectId);

            }

            Log.d(TAG, "onOrgListSuccess");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProjectListFailure() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "You do not have project yet!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMemberListSuccess(JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "You do have member yet!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMemberListFailure() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "You do not have project yet!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onTaskListSuccess(final JSONObject jsonObject) {


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                myTaskListModel = new ArrayList<>();
                try {
                    JSONArray taskList = (JSONArray) jsonObject.get("results");

                    for (int i = 0; i < taskList.length(); i++) {
                        JSONObject row = taskList.getJSONObject(i);

                        System.out.println("Increment! " + i);

                        MyTaskList myList = new MyTaskList(
                                row.get("id").toString(),
                                row.get("title").toString(),
                                row.get("description").toString()
                        );
                        System.out.println(myList);
                        System.out.println("Hello World!");
                        myTaskListModel.add(myList);
                    }
                    recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    adapter = new MyTaskListAdapter(myTaskListModel, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onTaskListFailure() {

    }


}
