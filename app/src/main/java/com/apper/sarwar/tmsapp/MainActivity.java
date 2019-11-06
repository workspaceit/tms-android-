package com.apper.sarwar.tmsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apper.sarwar.tmsapp.Adapter.ViewPagerAdapter;
import com.apper.sarwar.tmsapp.apiclients.auth.AuthRequestListener;
import com.apper.sarwar.tmsapp.datetimepicker.DateTimePickerFragment;
import com.apper.sarwar.tmsapp.entity.AuthUser;
import com.apper.sarwar.tmsapp.utils.LoadingMessageUtil;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AuthRequestListener {

    public static final String LOGIN_DATA = "LOGIN_DATA";
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private Context context;
//    private Context context;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setViewPager();
    }

    private void setViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (SharedPreferenceUtil.isLoggedIn(this)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onLoginSuccess(AuthUser authUser,String authorization) {
        Log.d(TAG, "onLoginSuccess:username " + authUser.getName());
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Sign In Success.", Toast.LENGTH_LONG).show();
            }
        });

        Log.d(TAG, authUser.getUid().toString());

        System.out.println(authUser.getUid().toString());


        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.userId, authUser.getUid(), this);
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.userName, authUser.getName(), this);
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgName, authUser.getOrg_name(), this);
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgId, authUser.getOrg_id(), this);
        SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.orgSlug, authUser.getOrg_slug(), this);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed(JSONObject failedMsg) {

        LoadingMessageUtil.endLoadingDialog();
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Invalid credentials given.", Toast.LENGTH_LONG).show();
            }
        });

        Log.d(TAG, "onLoginFailed: ");
    }

    @Override
    public void onSignUpSuccess(JSONObject jsonObject) {
        try {

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Successfully registered,Please check your mail.", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(this, MainActivity.class);
            System.out.println(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSignUpFailure(JSONObject jsonObject) {
        Log.d(TAG, "onSignUpFailure: ");
    }

    @Override
    public void onOrganizationSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onOrganizationFailure(JSONObject jsonObject) {

    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DateTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}
