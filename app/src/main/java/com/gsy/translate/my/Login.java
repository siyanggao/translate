package com.gsy.translate.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.App;
import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;
import com.gsy.translate.bean.User;
import com.gsy.translate.databinding.LoginBinding;
import com.gsy.translate.my.bean.UserBean;
import com.gsy.translate.utils.Tools;
import com.gsy.translate.utils.thirdlogin.ThirdLogin;
import com.gsy.translate.utils.thirdlogin.ThirdLoginCallBack;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * Created by Think on 2017/12/24.
 */

public class Login extends BaseActivity implements ThirdLoginCallBack {

    LoginBinding mBinding;
    ThirdLogin thirdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.login);
        initSharedPreferences();
        mBinding.setPresenter(new Presenter());
        thirdLogin = new ThirdLogin(this);
        thirdLogin.setCallBack(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = sharedPreferences.getString("myconfig",null);
        if(token!=null) this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        thirdLogin.setOnActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onSuccess(int type, String openid, int gender, String nickname, String avatar) {
        toThirdLogin(type,openid,gender,nickname,avatar);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public class Presenter{
        public void qqLogin(View v){
            thirdLogin.qq();
        }
        public void wxLogin(View v){thirdLogin.wx();}
        public void wbLogin(View v){
            thirdLogin.weibo();}
    }

    void toThirdLogin(int type,String openid,int sex,String nickname,String avatar){
        CommonOkHttpClient.post(com.gsy.translate.utils.Constants.baseUrl+"/user/thirdlogin")
                .addParam("type",1)
                .addParam("openid",openid)
                .addParam("sex",sex)
                .addParam("nickname",nickname)
                .addParam("avatar",avatar)
                .addListener(new DisposeDataListener<UserBean>(){
                    @Override
                    public void onSuccess(UserBean responseObj) {
                        if(responseObj.getCode()==1){
                            User user = responseObj.getData();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",user.getToken());
                            editor.putString("nickname",user.getNickname());
                            editor.putString("avatar",user.getAvatar());
                            editor.putInt("sex",user.getSex());
                            editor.commit();
                            App.token = user.getToken();
                            Login.this.finish();
                        }else{

                        }

                    }

                    @Override
                    public void onFailure(Exception resonObj) {
                        super.onFailure(resonObj);
                    }
                }).exec();
    }
}
