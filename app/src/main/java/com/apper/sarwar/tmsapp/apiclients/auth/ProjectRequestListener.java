package com.apper.sarwar.tmsapp.apiclients.auth;

import org.json.JSONObject;

public interface ProjectRequestListener {
    void onProjectAddedSuccess(JSONObject jsonObject);
    void onProjectAddedFailure();

}
