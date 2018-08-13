package com.gsy.translate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.gsy.translate.R;


/**
 * Created by gsy on 2016/9/19.
 */
public class LoadingPop extends PopupWindow {
    public LoadingPop(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.loading,null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
    }
}
