package com.apper.sarwar.tmsapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;

import com.apper.sarwar.tmsapp.R;

public class LoadingMessageUtil {
    private static ProgressDialog progressDialog;
    public static void startLoadingDialog(Activity activity,String loadingMessage){
        progressDialog = new ProgressDialog(activity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(loadingMessage);
        progressDialog.show();
    }

    public static void endLoadingDialog(){
        progressDialog.dismiss();
    }
}
