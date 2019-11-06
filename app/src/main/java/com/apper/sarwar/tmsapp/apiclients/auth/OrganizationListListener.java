package com.apper.sarwar.tmsapp.apiclients.auth;

import org.json.JSONObject;

public interface OrganizationListListener {

    void onOrganizationListSuccess(JSONObject jsonObject);

    void onOrganizationListFailure();

    void onProjectListSuccess(JSONObject jsonObject);

    void onProjectListFailure();

    void onMemberListSuccess(JSONObject jsonObject);

    void onMemberListFailure();

    void onTaskListSuccess(JSONObject jsonObject);

    void onTaskListFailure();


}
