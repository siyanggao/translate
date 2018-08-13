package com.gsy.translate.utils.thirdlogin;

/**
 * Created by Think on 2017/12/27.
 */

public interface ThirdLoginCallBack {
    public void onSuccess(int type,String openid,int gender,String nickname,String avatar);
    public void onFailure(String msg);
}
