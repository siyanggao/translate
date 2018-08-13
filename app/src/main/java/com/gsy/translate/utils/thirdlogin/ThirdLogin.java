package com.gsy.translate.utils.thirdlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.R;
import com.gsy.translate.my.Login;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.Tools;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Think on 2017/12/27.
 *
 */

public class ThirdLogin {

    Tencent mTencent;
    public BaseUiListener qqListener;
    IWXAPI iwxapi;
    AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    Context context;
    ThirdLoginCallBack callBack;
    public final int TYPE_QQ = 0;
    public final int TYPE_WEIXIN = 1;
    public final int TYPE_WEIBO = 2;

    public ThirdLogin(Context context){
        this.context = context;
    }

    //qq登录
    public void qq(){
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, context);
        if (!mTencent.isSessionValid())
        {
            qqListener = new BaseUiListener();
            mTencent.login((Activity) context, "get_user_info", qqListener);
        }
    }
    //调用qq登录回调
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            JSONObject response = (JSONObject)o;
            try {
                final String expires_in = response.getString("expires_in");
                final String openid = response.getString("openid");
                final String access_token = response.getString("access_token");
                mTencent.setOpenId(openid);
                mTencent.setAccessToken(access_token, expires_in);
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onError(UiError e) {
                        if(callBack!=null) callBack.onFailure(e.errorMessage);
                    }
                    @Override
                    public void onComplete(final Object response) {
                        JSONObject json = (JSONObject)response;
                        try {
                            String nickname = json.getString("nickname");
                            String gender = json.getString("gender");
                            String figureurl_qq_2 = json.getString("figureurl_qq_2");
                            int sex;
                            if(gender.equals("男")) sex = 1;
                            else if(gender.equals("女")) sex = 2;
                            else sex = 0;
                            if(callBack!=null) callBack.onSuccess(TYPE_QQ,openid,sex,nickname,figureurl_qq_2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callBack.onFailure(e.getMessage());
                        }
                    }
                    @Override
                    public void onCancel() {if(callBack!=null) callBack.onFailure("用户取消");}
                };
                UserInfo mInfo = new UserInfo(context, mTencent.getQQToken());
                mInfo.getUserInfo(listener);
            } catch (JSONException e) {
                e.printStackTrace();
                callBack.onFailure(e.getMessage());
            }
        }
        @Override
        public void onError(UiError e) {
            System.out.println(e.errorMessage);
            if(callBack!=null) callBack.onFailure(e.errorMessage);
        }
        @Override
        public void onCancel() {
            if(callBack!=null) callBack.onFailure("用户取消");
        }
    }
    public void setOnActivityResult(int requestCode, int resultCode, Intent data){
        if(qqListener!=null) Tencent.onActivityResultData(requestCode,resultCode,data,qqListener);
        if (mSsoHandler != null) mSsoHandler.authorizeCallBack(requestCode, resultCode, data);

    }

    //微信登录
    public void wx(){
        regToWx();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "login";
        iwxapi.sendReq(req);
    }
    //注册到微信
    public void regToWx(){
        iwxapi = WXAPIFactory.createWXAPI(context,Constants.WX_APP_ID,true);
        iwxapi.registerApp(Constants.WX_APP_ID);
    }

    //微博登录
    public void weibo(){
        mAuthInfo = new AuthInfo(context, Constants.WB_APP_ID, "http://www.gsy.com", "");
        WbSdk.install(context,mAuthInfo);
        mSsoHandler = new SsoHandler((Activity) context);
        mSsoHandler.authorizeClientSso(new SelfWbAuthListener());
    }
    //微博登录回调
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (token.isSessionValid()) {
                        CommonOkHttpClient.get("https://api.weibo.com/2/users/show.json")
                                .addParam("access_token",token.getToken())
                                .addParam("uid",token.getUid())
                                .addListener(new DisposeDataListener<String>(){
                                    @Override
                                    public void onSuccess(String responseObj) {
                                        try {
                                            JSONObject json = new JSONObject(responseObj);
                                            String nickname = json.getString("screen_name");
                                            String gender = json.getString("gender");//m：男、f：女、n：未知
                                            String avatar = json.getString("profile_image_url");
                                            int sex = 0;
                                            if(gender.equals("m")) sex = 1;
                                            else if(gender.equals("f")) sex = 2;
                                            if(callBack!=null) callBack.onSuccess(2,token.getUid(),sex,nickname,avatar);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            if(callBack!=null) callBack.onFailure("获取用户失败");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Exception resonObj) {
                                        super.onFailure(resonObj);
                                        if(callBack!=null) callBack.onFailure(resonObj.getMessage());
                                    }
                                }).exec();
                    }
                }
            });
        }
        @Override
        public void cancel() {
            Toast.makeText(context,"用户取消", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(context, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setCallBack(ThirdLoginCallBack callBack) {
        this.callBack = callBack;
    }
}
