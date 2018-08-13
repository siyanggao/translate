package com.gsy.okhttp;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by gsy on 2017/1/22.
 */

public class CommonOkHttpClient {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    static{
        OkHttpClient.Builder okHttpBuiler = new OkHttpClient.Builder();
        okHttpBuiler.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        okHttpBuiler.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuiler.readTimeout(TIME_OUT,TimeUnit.SECONDS);
        okHttpBuiler.writeTimeout(TIME_OUT,TimeUnit.SECONDS);
        okHttpBuiler.followRedirects(true);
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpBuiler.sslSocketFactory(sslSocketFactory,trustManager);
        }catch (Exception e){
            e.printStackTrace();
        }
        mOkHttpClient = okHttpBuiler.build();
    }

    public static <ResultType> Call get(String url,RequestParams params,DisposeDataListener<ResultType> listener){
        Request request = CommonRequest.createGetRequest(url,params);
        Call call = mOkHttpClient.newCall(request);
        DisposeDataHandler handle = new DisposeDataHandler(listener);
        call.enqueue(new CommonJsonCallBack(handle));
        return call;
    }

    public static Call post(String url,RequestParams params,DisposeDataListener listener){
        Request request = CommonRequest.createPostRequest(url,params);
        Call call = mOkHttpClient.newCall(request);
        DisposeDataHandler handle = new DisposeDataHandler(listener);
        call.enqueue(new CommonJsonCallBack(handle));
        return call;
    }

    public static Call multiPost(String url,RequestParams params,DisposeDataListener listener){
        Request request = CommonRequest.createMultiPostRequest(url,params);
        Call call = mOkHttpClient.newCall(request);
        DisposeDataHandler handle = new DisposeDataHandler(listener);
        call.enqueue(new CommonJsonCallBack(handle));
        return call;
    }

    public static Call downloadFile(String url,RequestParams params,DisposeDownloadListener listener,String source){
        Request request = CommonRequest.createGetRequest(url,params);
        Call call = mOkHttpClient.newCall(request);
        DisposeDataHandler handle = new DisposeDataHandler(listener,source);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }

    public static OkHttpBuilder get(String url){
        return new OkHttpBuilder(url,0);
    }

    public static OkHttpBuilder post(String url){
        return new OkHttpBuilder(url,1);
    }

    public static OkHttpBuilder multiPost(String url){
        return new OkHttpBuilder(url,2);
    }

    public static OkHttpBuilder downloadFile(String url){
        return new OkHttpBuilder(url,3);
    }

//    public static Call post(Request request,DisposeDataHandler handle){
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new CommonJsonCallBack(handle));
//        return call;
//    }
//
//    public static Call get(Request request,DisposeDataHandler handle){
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new CommonJsonCallBack(handle));
//        return call;
//    }
//
//    public static Call downloadFile(Request request, DisposeDataHandler handle)
//    {
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new CommonFileCallback(handle));
//        return call;
//    }



}
