package com.gsy.translate.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * 自定义Toast，解决重复显示的问题
 * @author gsy
 */
public class CustomToast {
	private static Toast mToast;

	public static Toast makeText(Context mContext,String text){
		return makeText(mContext,text,Toast.LENGTH_SHORT);
	}
	public static Toast makeText(Context mContext, String text, int duration) {

		if (mToast != null)
			 mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, duration);

		return mToast;

	}
	public static Toast makeText(Context mContext, int resId, int duration) {
		return makeText(mContext, mContext.getResources().getString(resId), duration);

	}



}
