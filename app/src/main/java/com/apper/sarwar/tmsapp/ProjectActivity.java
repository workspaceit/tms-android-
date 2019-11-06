package com.apper.sarwar.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.apiclients.auth.ProjectClient;
import com.apper.sarwar.tmsapp.apiclients.auth.ProjectRequestListener;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;

import org.json.JSONObject;

public class ProjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProjectRequestListener {

    NavigationView navigationView;
    Button project_button;
    EditText project_name, project_description;
    String urlAuthorization, orgName,orgSlug;
    ProjectClient projectClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        projectClient = new ProjectClient(this);


        orgName = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName, this);
        orgSlug = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgSlug, this);
        urlAuthorization = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.urlAuthorization, this);

        project_button = (Button) findViewById(R.id.project_button);
        project_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project_name = (EditText) findViewById(R.id.project_name);
                project_description = (EditText) findViewById(R.id.project_description);
                String select_project_name = project_name.getText().toString().trim();
                String select_project_description = project_description.getText().toString().trim();
                addProject(select_project_name, select_project_description, orgSlug, urlAuthorization);
            }
        });


    }

    private void addProject(String select_project_name, String select_project_description, String orgSlug, String urlAuthorization) {
        LoadingMessageUtil.startLoadingDialog(this, "Processing..");
        projectClient.addProject(select_project_name, select_project_description, orgSlug, urlAuthorization);
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

        }

       /* else if (id == R.id.nav_logout) {
            LoadingMessageUtil.startLoadingDialog(this, "Log outing...");
            SharedPreferenceUtil.logOut(this);
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onProjectAddedSuccess(JSONObject jsonObject) {
        try {

            /*String orgSlug = (String) jsonObject.get("slug");*/
            System.out.println(jsonObject);
            LoadingMessageUtil.endLoadingDialog();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Project added successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProjectAddedFailure() {

        LoadingMessageUtil.endLoadingDialog();
        try {

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
