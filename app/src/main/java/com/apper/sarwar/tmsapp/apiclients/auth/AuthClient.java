package com.apper.sarwar.tmsapp.apiclients.auth;

import android.content.Context;
import android.util.Log;

import com.apper.sarwar.tmsapp.configuration.HttpConfiguration;
import com.apper.sarwar.tmsapp.entity.AuthUser;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;
import com.apper.sarwar.tmsapp.viewmodel.OrganizationForm;
import com.apper.sarwar.tmsapp.viewmodel.ProjectForm;
import com.apper.sarwar.tmsapp.viewmodel.SignUpForm;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import okhttp3.ResponseBody;

public class AuthClient {
    private static final String TAG = "AuthClient";
    private AuthRequestListener listener;
    private Context context;

    public AuthClient(Context context) {
        this.context=context;
        listener = (AuthRequestListener) context;
    }


    public void login(String username, String password){

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String requestUrl=HttpConfiguration.BASE_URL+"/o/token/";

        RequestBody requestBody = new FormBody.Builder()
            .add("client_id", HttpConfiguration.CLIENT_ID)
                .add("client_secret", HttpConfiguration.CLIENT_SECRET)
                .add("grant_type", "password")
                .add("username", username)
                .add("password", password)
                .build();

        final Request httpRequest = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(httpRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure()");
                call.cancel();
                JSONObject failResponse = new JSONObject();
                try{
                    failResponse.put("response_body","");
                    failResponse.put("response_code",404);
                    failResponse.put("response_message","login failed");
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                listener.onLoginFailed(failResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    System.out.println("onResponse()");
                    String responseBody = response.body().string();


                    JSONObject responseObject = new JSONObject(responseBody);

                    if(response.code()==200){
                        String authorization = responseObject.getString("token_type")+" "+responseObject.getString("access_token");
                        Request request = new Request.Builder()
                                .header("Authorization", authorization)
                                .url(HttpConfiguration.BASE_URL+"/api/get-user-info/")
                                .build();

                        Response authUserResponse = new OkHttpClient().newCall(request).execute();
                        ResponseBody authResponseBody = authUserResponse.body();
                        String authUserJsonBody = authResponseBody.string();

                        AuthUser authUser = new ObjectMapper().readValue(authUserJsonBody,AuthUser.class);
                        /*SharedPreferenceUtil.setDefaults(SharedPreferenceUtil.urlAuthorization, authorization, this);*/

                        listener.onLoginSuccess(authUser,authorization);
                    }else{
                        listener.onLoginFailed(responseObject);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }



            }
        });
    }

    public void signUp(SignUpForm signUpForm){
        try{
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String requestUrl=HttpConfiguration.BASE_URL+"/api/user-register/";


            String jsonString = new ObjectMapper().writeValueAsString(signUpForm);


            RequestBody requestBody = FormBody.create(JSON,jsonString);
            Request httpRequest = new Request.Builder()
                    .url(requestUrl)
                    .post(requestBody)
                    .build();

            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure()");
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if(response.code()==201){
                            listener.onSignUpSuccess(responseObject);
                        }else{
                            listener.onSignUpFailure(responseObject);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "signUp: "+e.getMessage());
        }

    }


    public void createOrganization(OrganizationForm organizationForm,String authorization){
        try{
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String requestUrl=HttpConfiguration.BASE_URL+"/api/organizations/";


            String jsonString = new ObjectMapper().writeValueAsString(organizationForm);


            RequestBody requestBody = FormBody.create(JSON,jsonString);
            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .post(requestBody)
                    .build();



            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure()");
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if(response.code()==201){

                            listener.onOrganizationSuccess(responseObject);

                        }else{
                            listener.onOrganizationFailure(responseObject);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "signUp: "+e.getMessage());
        }
    }


    public void createProject(ProjectForm projectForm, String authorization){

        try{
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String requestUrl=HttpConfiguration.BASE_URL+"/api/wsit_wsit_wsit/projects/";


            String jsonString = new ObjectMapper().writeValueAsString(projectForm);


            RequestBody requestBody = FormBody.create(JSON,jsonString);
            Request httpRequest = new Request.Builder()
                    .header("Authorization", authorization)
                    .url(requestUrl)
                    .post(requestBody)
                    .build();



            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure()");
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        System.out.println("onResponse()");
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody);
                        if(response.code()==201){
                            listener.onOrganizationSuccess(responseObject);
                        }else{
                            listener.onOrganizationFailure(responseObject);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "signUp: "+e.getMessage());
        }
    }



}
