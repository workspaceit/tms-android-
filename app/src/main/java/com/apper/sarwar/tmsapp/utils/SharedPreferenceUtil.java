package com.apper.sarwar.tmsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private static final int MODE_PRIVATE = 0x0000;
    public static final String userId = "userId";
    public static final String userName = "userName";
    private static final String AUTH_USER_PREF = "AUTH_USER_PREF";
    public static final String urlAuthorization = "urlAuthorization";
    public static final String orgId = "orgId";
    public static final String orgSlug = "orgSlug";
    public static final String orgName = "orgName";
    public static final String projectId = "projectId";


    public static void setDefaults(String key, String value, Context context) {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, MODE_PRIVATE);
        String value = pref.getString(key, null);
        return value;
    }

    public static boolean isLoggedIn(Context context) {
        String userId = SharedPreferenceUtil.getDefaults(SharedPreferenceUtil.userId, context);
        if (userId != null && !userId.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void logOut(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SharedPreferenceUtil.AUTH_USER_PREF, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


}
