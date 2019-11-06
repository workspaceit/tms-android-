package com.apper.sarwar.tmsapp.apiclients.auth;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.tmsapp.configuration.HttpConfiguration;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProjectClient {

    private static final String TAG = "AuthOrganizationClient";
    private ProjectRequestListener listener;
    private ProjectRequestListListener projectRequestListListener;
    private Context context;
    final OkHttpClient okHttpClient = new OkHttpClient();


    public ProjectClient(Context context) {
        this.context = context;
        listener = (ProjectRequestListener) context;
        projectRequestListListener = (ProjectRequestListListener) context;
    }

    public void addProject(String projectTitle, String projectDescription, String orgSlug, String authorization) {

        try {
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String requestUrl = HttpConfiguration.BASE_URL + "/api/" + orgSlug + "/projects/";
            //String requestUrl = "http://58.84.34.65:8585/api/organizations/606807b89f914cd398b502044d0ed656/";


            RequestBody requestBody = new FormBody.Builder()
                    .add("name", projectTitle)
                    .add("description", projectDescription)
                    .add("manager", "")
                    .build();

            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .post(requestBody)
                    .build();


            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onProjectAddedFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if (response.code() == 201) {
                            listener.onProjectAddedSuccess(responseObject);
                        } else {
                            listener.onProjectAddedFailure();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "signUp: " + e.getMessage());
        }
    }

    public void getAllProjectByOrg(String authorization, String orgSlug) {
        try {
            Request request = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(HttpConfiguration.BASE_URL + "api/" + orgSlug + "/projects")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    projectRequestListListener.onProjectListFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        projectRequestListListener.onProjectListSuccess(responseObject);
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
