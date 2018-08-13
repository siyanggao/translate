package com.gsy.translate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsy.translate.view.LoadingPop;
import com.gsy.translate.view.NoNetworkPop;

/**
 * Created by gsy on 2016/9/28.
 */
public class BaseActivity extends Activity {
    protected SharedPreferences sharedPreferences;

    protected ImageView titlebar_left;
    protected TextView titlebar_center;
    protected TextView titlebar_right;
    protected ImageView titlebar_right2;
    LoadingPop loadingPop;
    protected NoNetworkPop noNetworkPop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showLoadingPop(){
        if(loadingPop==null) {
            loadingPop = new LoadingPop(BaseActivity.this);
        }
        loadingPop.showAsDropDown(titlebar_center);
    }
    protected void hideLoadingPop(){
        if(loadingPop!=null&&loadingPop.isShowing()&&hasWindowFocus()) loadingPop.dismiss();
    }
    protected void showNoNetWorkPop(){
        if(loadingPop!=null) loadingPop.dismiss();
        if(noNetworkPop==null)  {
            noNetworkPop = new NoNetworkPop(this);
            noNetworkPop.setBackgroundDrawable(new ColorDrawable());
        }
        noNetworkPop.showAsDropDown(titlebar_center);
    }

    public void initSharedPreferences() {
        sharedPreferences = getSharedPreferences("myconfig",MODE_PRIVATE);
    }
}
