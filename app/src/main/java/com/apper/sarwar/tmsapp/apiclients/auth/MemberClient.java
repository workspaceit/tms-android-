package com.apper.sarwar.tmsapp.apiclients.auth;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.tmsapp.configuration.HttpConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MemberClient {

    private static final String TAG = "AuthOrganizationClient";
    private MemberRequestListener listener;
    private Context context;
    final OkHttpClient okHttpClient = new OkHttpClient();


    public MemberClient(Context context) {
        this.context = context;
        listener = (MemberRequestListener) context;
    }

    public void addMember(String orgId, String memberId, String authorization) {
        try {
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String requestUrl = HttpConfiguration.BASE_URL + "/api/organizations/" + orgId+"/";
            //String requestUrl = "http://58.84.34.65:8585/api/organizations/606807b89f914cd398b502044d0ed656/";



            List memberIds=new ArrayList();
            memberIds.add(memberId);

            HashMap<String,Object> memberObj=new HashMap<>();
            memberObj.put("member_ids",memberIds);

            String jsonString = new ObjectMapper().writeValueAsString(memberObj);
            RequestBody requestBody = FormBody.create(JSON,jsonString);

            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .patch(requestBody)
                    .build();





            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onMemberAddedFailure();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if (response.code() == 200) {
                            listener.onMemberAddedSuccess(responseObject);
                        } else {
                            listener.onMemberAddedFailure();
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
}
