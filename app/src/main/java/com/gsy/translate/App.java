package com.gsy.translate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/9/19.
 */
public class App extends Application {
    public SharedPreferences sharedPreferences;
    public static String token;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("myconfig",MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();


//

    }


    public static String getToken() {
        return token;
    }
}
