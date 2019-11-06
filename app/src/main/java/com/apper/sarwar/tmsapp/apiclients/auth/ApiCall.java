package com.apper.sarwar.tmsapp.apiclients.auth;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apper.sarwar.tmsapp.configuration.HttpConfiguration;
import com.apper.sarwar.tmsapp.utils.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Map;

import static com.apper.sarwar.tmsapp.configuration.HttpConfiguration.BASE_URL;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String orgId=SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.orgId, ctx);
        String url = BASE_URL+"/api/all-members/?search_key=" + query
                + "&org_id="+orgId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener){};
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}