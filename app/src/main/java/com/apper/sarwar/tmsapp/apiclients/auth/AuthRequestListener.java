package com.apper.sarwar.tmsapp.apiclients.auth;

import com.apper.sarwar.tmsapp.entity.AuthUser;

import org.json.JSONObject;

public interface AuthRequestListener {

    void onLoginSuccess(AuthUser authUser,String authorization);
    void onLoginFailed(JSONObject jsonObject);
    void onSignUpSuccess(JSONObject jsonObject);
    void onSignUpFailure(JSONObject jsonObject);

    void onOrganizationSuccess(JSONObject jsonObject);
    void onOrganizationFailure(JSONObject jsonObject);
}
