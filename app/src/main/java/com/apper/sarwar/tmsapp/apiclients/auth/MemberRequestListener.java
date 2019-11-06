package com.apper.sarwar.tmsapp.apiclients.auth;

import org.json.JSONObject;

public interface MemberRequestListener {
    void onMemberAddedSuccess(JSONObject jsonObject);
    void onMemberAddedFailure( );
}
