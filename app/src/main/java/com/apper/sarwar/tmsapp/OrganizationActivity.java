package com.apper.sarwar.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.apiclients.auth.AuthClient;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthRequestListener;
import com.apper.sarwar.tmsapp.entity.AuthUser;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.apper.sarwar.tmsapp.viewmodel.OrganizationForm;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;


public class OrganizationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AuthRequestListener {

    private final String TAG = "OrganizationActivity";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView user_name_singed;
    Toolbar toolbar;
    Button orgSave;
    EditText organizationName, organizationSlug, organizationEmail, organizationDescription;
    AuthClient authClient;
    String urlAuthorization;
    MenuItem app_bar_switch_organization;
    Switch onOffSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /**
         * Create Organization Form Data
         */

        try {

            authClient = new AuthClient(this);

            orgSave = (Button) findViewById(R.id.orgSave);
            urlAuthorization = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.urlAuthorization, this);


            orgSave.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    organizationName = (EditText) findViewById(R.id.organization_name);
                    organizationSlug = (EditText) findViewById(R.id.organization_slug);
                    organizationEmail = (EditText) findViewById(R.id.organization_email);
                    organizationDescription = (EditText) findViewById(R.id.organization_description);

                    String nameText = organizationName.getText().toString().trim();
                    String slugText = organizationSlug.getText().toString().trim();
                    String emailText = organizationEmail.getText().toString().trim();
                    String descriptionText = organizationDescription.getText().toString().trim();

                    OrganizationForm organizationForm = new OrganizationForm(nameText, slugText, emailText, descriptionText);
                    try {
                        if (isValidForm(view, nameText, slugText, emailText, descriptionText)) {
                            createOrganization(organizationForm, urlAuthorization);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e.getMessage());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem app_bar_switch_organization = menu.findItem(R.id.app_bar_switch_organization);
        app_bar_switch_organization.setTitle(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName,this));
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name_singed);
        String userId= SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userId,this);
        navUsername.setText(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userName,this));

        try {
            onOffSwitch = (Switch) headerView.findViewById(R.id.nav_switch_button);
            onOffSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        navigationView.getMenu().setGroupVisible(R.id.nav_main_section, false);
                        navigationView.getMenu().setGroupVisible(R.id.nav_settings_section, true);

/*
                        Toast.makeText(HomeActivity.this, "Show the settings section : " + isChecked, Toast.LENGTH_SHORT).show();
*/
                    } else {
                        navigationView.getMenu().setGroupVisible(R.id.nav_main_section, true);
                        navigationView.getMenu().setGroupVisible(R.id.nav_settings_section, false);
/*
                        Toast.makeText(HomeActivity.this, "Show the Item menu section section : " + isChecked, Toast.LENGTH_SHORT).show();
*/
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  createOrganization( OrganizationForm organizationForm,String urlAuthorization){
        try {
            LoadingMessageUtil.startLoadingDialog(this, "Processing..");
            authClient.createOrganization(organizationForm, urlAuthorization);

        } catch (Exception e) {
            Log.d(TAG, "onResponse: " + e.getMessage());
        }
    }

    public boolean isValidForm(View view, String orgName, String orgSlug, String orgEmail, String orgDesc) {

        boolean validOrgName = !orgName.isEmpty();

        if (!validOrgName) {
            Snackbar.make(view, "Organization Name Can't Be Empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return validOrgName;
        }

        boolean validOrgSlug = !orgSlug.isEmpty();

        if (!validOrgSlug) {
            Snackbar.make(view, "Organization Slug Can't Be Empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return validOrgSlug;
        }

        boolean validOrgEmail = !orgEmail.isEmpty();

        if (!validOrgEmail) {
            Snackbar.make(view, "Organization Email Can't Be Empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return validOrgEmail;
        }

        boolean validEmail = EmailValidator.getInstance().isValid(orgEmail);

        if (!validEmail) {
            Snackbar.make(view, "Please Enter Valid Email!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return validEmail;
        }

        boolean validOrgDesc = !orgDesc.isEmpty();

        if (!validOrgDesc) {
            Snackbar.make(view, "Organization Description Can't Be Empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return validOrgDesc;
        }


        return validOrgName && validOrgSlug && validOrgEmail && validEmail && validOrgDesc;
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

        }else if (id == R.id.add_member) {
            Intent intentProject = new Intent(this, MemberActivity.class);
            startActivity(intentProject);

        }/* else if (id == R.id.nav_logout) {
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
    public void onLoginSuccess(AuthUser authUser, String authorization) {

    }

    @Override
    public void onLoginFailed(JSONObject jsonObject) {

    }

    @Override
    public void onSignUpSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onSignUpFailure(JSONObject jsonObject) {

    }

    @Override
    public void onOrganizationSuccess(JSONObject jsonObject) {


        try {

            LoadingMessageUtil.endLoadingDialog();

            String orgId = (String) jsonObject.get("id");
            String orgSlug = (String) jsonObject.get("slug");
            String orgName= (String) jsonObject.get("name");

            SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgId, orgId, this);
            SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgSlug, orgSlug, this);
            SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgName, orgName, this);

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            app_bar_switch_organization = menu.findItem(R.id.app_bar_switch_organization);

            runOnUiThread(new Runnable() {
                public void run() {
                    app_bar_switch_organization.setTitle(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName,getApplication()));
                    Toast.makeText(getApplicationContext(), "Organization created successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onOrganizationFailure(JSONObject jsonObject) {
        try {
            LoadingMessageUtil.endLoadingDialog();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Organization with this slug already exists!", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            System.out.println(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
