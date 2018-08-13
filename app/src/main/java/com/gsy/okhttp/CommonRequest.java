package com.gsy.okhttp;


import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gsy on 2017/1/22.
 */

public class CommonRequest {

    public static Request createGetRequest(String url, RequestParams params){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if(params!=null){
            for(Map.Entry<String,String> entry:params.urlParams.entrySet()){
                urlBuilder.append(entry.getKey()+"="+entry.getValue()).append("&");
            }
        }
        return  new Request.Builder().url(urlBuilder.substring(0,urlBuilder.length()-1)).get().build();
    }

    public static Request createPostRequest(String url,RequestParams params){
        FormBody.Builder builder = new FormBody.Builder();
        if(params!=null){
            for(Map.Entry<String,String> entry:params.urlParams.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url,RequestParams params){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if(params!=null){
            for(Map.Entry<String,Object> entry:params.fileParams.entrySet()){
                if(entry.getValue() instanceof File){
                    File file = (File) entry.getValue();
                    builder.addFormDataPart(entry.getKey(),
                            file.getName(),
                            RequestBody.create(FILE_TYPE,(File) entry.getValue()));
                }else{
                    builder.addFormDataPart(entry.getKey(),String.valueOf(entry.getValue()));
                }
            }
            for(Map.Entry<String,String> entry:params.urlParams.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }
}
