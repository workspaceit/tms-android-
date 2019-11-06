package com.apper.sarwar.tmsapp.apiclients.auth;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.tmsapp.configuration.HttpConfiguration;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizationClient {
    private static final String TAG = "OrganizationClient";
    private OrganizationListListener organizationListListener;
    private Context context;

    public OrganizationClient(Context context) {
        this.context = context;
        organizationListListener = (OrganizationListListener) context;
    }

    public void getAllOrganization(String authorization) {

        try {

            Request request = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(HttpConfiguration.BASE_URL + "/api/organizations/")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    organizationListListener.onOrganizationListFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        organizationListListener.onOrganizationListSuccess(responseObject);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
        }
    }

    public void getAllProjectByOrg(String authorization, String orgSlug) {
        try {
            Request request = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(HttpConfiguration.BASE_URL + "/api/" + orgSlug + "/projects")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    organizationListListener.onProjectListFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        organizationListListener.onProjectListSuccess(responseObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
        }
    }

    public void getMemberByOrg(String authorization, String orgId) {
        try {
            Request request = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(HttpConfiguration.BASE_URL + "/api/organizations-members/?org_id=" + orgId)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    organizationListListener.onMemberListFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        organizationListListener.onMemberListSuccess(responseObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
        }
    }

    public void getAllTaskByProject(String authorization, String orgSlug, String projectId) {
        try {
            Request request = new Request.Builder()
                    .header("Authorization", authorization)
                    .url("http://58.84.34.65:8585/api/d3/projects/5e0b908ac8b34a268b741d2dcb2cdc6d/tasks/")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    organizationListListener.onTaskListFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        organizationListListener.onTaskListSuccess(responseObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
        }
    }
}
