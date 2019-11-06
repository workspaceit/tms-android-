package com.apper.sarwar.tmsapp.apiclients.auth;

import org.json.JSONObject;

public interface ProjectRequestListListener {
    void onProjectListSuccess(JSONObject jsonObject);

    void onProjectListFailure();
}
