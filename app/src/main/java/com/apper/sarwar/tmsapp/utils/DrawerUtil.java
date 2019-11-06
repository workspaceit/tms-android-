package com.apper.sarwar.tmsapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.HomeActivity;
import com.apper.sarwar.tmsapp.MainActivity;
import com.apper.sarwar.tmsapp.MemberActivity;
import com.apper.sarwar.tmsapp.OrganizationActivity;
import com.apper.sarwar.tmsapp.ProjectActivity;
import com.apper.sarwar.tmsapp.R;
import com.apper.sarwar.tmsapp.apiclients.auth.OrganizationClient;
import com.apper.sarwar.tmsapp.apiclients.auth.OrganizationListListener;

import org.json.JSONObject;


public class DrawerUtil {

    public static NavigationView navigationView;
    public static Menu menu;
    public static Switch onOffSwitch;
    Activity activity;
    public static String authorizationToken;
    private static final String TAG = "Drawer Utils";
    private static OrganizationClient organizationClient;


    public DrawerUtil(Activity activity) {
        this.activity = activity;
        organizationClient = new OrganizationClient(activity);
    }

    public static void getDrawer(final Activity activity, Toolbar toolbar) {

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) activity.findViewById(R.id.nav_view);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name_singed);
        String userId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userId, activity);
        navUsername.setText(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userName, activity));

        navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        MenuItem app_bar_switch_organization = menu.findItem(R.id.app_bar_switch_organization);
        app_bar_switch_organization.setTitle(SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgName, activity.getApplicationContext()));

        authorizationToken=SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.urlAuthorization, activity);
        organizationClient = new OrganizationClient(activity.getApplicationContext());
        organizationClient.getAllOrganization(authorizationToken);


        MenuItem nav_organization_item = menu.findItem(R.id.nav_organization);
        SubMenu subMenu = nav_organization_item.getSubMenu();
        subMenu.add(255, 1, 0, "TEST").setTitleCondensed("fdfdffdfddfd878787777dsdddsd");
        subMenu.add(255, 2, 0, "TEST one").setTitleCondensed("fdfdffdfddfd878787777");


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

    }

    public static void getNavigationItemSelected(MenuItem item, Activity activity) {
        int id = item.getItemId();

        if (id == R.id.create_organization) {
            Intent intent = new Intent(activity, OrganizationActivity.class);
            activity.startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
        } else if (id == R.id.create_project) {
            Intent intentProject = new Intent(activity, ProjectActivity.class);
            activity.startActivity(intentProject);

        } else if (id == R.id.add_member) {
            Intent intentProject = new Intent(activity, MemberActivity.class);
            activity.startActivity(intentProject);

        } else if (id == R.id.nav_logout) {
            LoadingMessageUtil.startLoadingDialog(activity, "Log outing...");
            SharedPreferenceUtil.logOut(activity);
            activity.finish();
            activity.startActivity(new Intent(activity, MainActivity.class));
        } else if (item.getGroupId() == 255) {
            Toast.makeText(activity, item.getTitleCondensed(), Toast.LENGTH_SHORT).show();
        }else if (item.getGroupId() == 256) {
            Toast.makeText(activity, item.getTitleCondensed(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_my_settings) {

            Toast.makeText(activity, item.getTitleCondensed(), Toast.LENGTH_SHORT).show();
        }
    }
}
