package com.gsy.okhttp;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.RequestBody;

/**
 * Created by siyang on 2017/8/14.
 */

public class OkHttpBuilder {
    String url;
    RequestParams params;

    DisposeDataListener listener;
    DisposeDownloadListener downloadListener;
    String source;
    int method;//0,get;1,post;2,multipost;3,downloadfile
    public OkHttpBuilder(String url,int method){
        this.url = url;
        this.method = method;
        params = new RequestParams();
    }
    public OkHttpBuilder addParam(String key,Object value){
        if(key==null||value==null)
            return this;
        if(value instanceof File){
            try {
                params.put(key,value);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            params.put(key,value.toString());
        }

        return this;
    }

    public<ResultType> OkHttpBuilder addListener(DisposeDataListener<ResultType> listener){
        this.listener = listener;
        return this;
    }

    public<ResultType> OkHttpBuilder addListener(DisposeDownloadListener downloadListener){
        this.downloadListener = downloadListener;
        return this;
    }

    public OkHttpBuilder addSource(String source){
        this.source = source;
        return this;
    }

    public void exec(){
        switch (method){
            case 0:
                CommonOkHttpClient.get(url,params,listener);
                break;
            case 1:
                CommonOkHttpClient.post(url,params,listener);
                break;
            case 2:
                CommonOkHttpClient.multiPost(url,params,listener);
                break;
            case 3:
                CommonOkHttpClient.downloadFile(url,params,downloadListener,source);
                break;
        }

    }
}
