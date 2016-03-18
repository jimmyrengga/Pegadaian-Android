package com.pegadaian.aplikasi.cobalagi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pegadaian.aplikasi.cobalagi.constant.Constant;
import com.pegadaian.aplikasi.cobalagi.service.HttpService;

/**
 * Created by opaw on 3/18/16.
 */
public class ActivityHelper extends AppCompatActivity {
    public HttpService service = new HttpService();

    public AlertDialog.Builder loadingBuilder, messageBuilder, builderListDevice = null;
    public AlertDialog loadingDialog, messageDialog = null;

    public void closeSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public <T> T getUserCookie(String key, Class<T> a) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS, Context.MODE_PRIVATE);

        if (sharedPreferences == null) {
            return null;
        }

        String data = sharedPreferences.getString(key, null);

        if (data == null) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(data, a);
        }
    }

    public void setUserCookies(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getHttpCookie(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS, Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return null;
        }
        return  sharedPreferences.getString(Constant.KEY_COOKIES, null);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showLoadingDialog(String text) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
        TextView loadingText = (TextView) promptsView.findViewById(R.id.loading_text);
        loadingText.setText(text);
        if(loadingBuilder == null) {
            loadingBuilder = new AlertDialog.Builder(this);
        }

        loadingBuilder.setView(promptsView);
        loadingBuilder.setCancelable(false);

        loadingDialog = loadingBuilder.create();
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }

    public void showMessageDialog(String msg){
        if(messageBuilder == null) {
            messageBuilder = new AlertDialog.Builder(this);
        }
        messageBuilder.setMessage(msg);
        messageBuilder.setCancelable(false);
        messageBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        messageDialog = messageBuilder.create();
        messageDialog.show();
    }
}
