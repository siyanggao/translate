package com.gsy.translate.wxapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.bean.User;
import com.gsy.translate.my.Login;
import com.gsy.translate.my.bean.UserBean;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.Tools;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	IWXAPI api;
	//SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this,Constants.WX_APP_ID, false);
		api.handleIntent(getIntent(), this);
		//sharedPreferences = getSharedPreferences("myconfig", MODE_PRIVATE);

		
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Tools.l(resp.transaction);
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Toast.makeText(this, "用户取消", Toast.LENGTH_SHORT).show();

			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Toast.makeText(this, "用户取消", Toast.LENGTH_SHORT).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Toast.makeText(this, "用户拒绝", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		this.finish();
	}


}