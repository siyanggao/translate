package com.gsy.okhttp;

/**
 * Created by gsy on 2017/1/22.
 */

public class OkHttpException extends Exception {


    private int ecode;

    public OkHttpException(int ecode,String emsg){
        super(emsg);
        this.ecode = ecode;

    }

    public int getEcode() {
        return ecode;
    }

}
