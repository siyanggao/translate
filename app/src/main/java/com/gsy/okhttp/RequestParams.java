package com.gsy.okhttp;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gsy on 2017/1/22.
 */

public class RequestParams {
    public ConcurrentHashMap<String,String> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,Object> fileParams = new ConcurrentHashMap<>();

    public RequestParams(){
        this((Map<String, String>) null);
    }

    public RequestParams(Map<String,String> source){
        if(source!=null){
            for(Map.Entry<String,String> entry:source.entrySet()){
                put(entry.getKey(),entry.getValue());
            }
        }
    }

    public void put(String key,String value){
        if(key!=null&&value!=null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            fileParams.put(key, object);
        }
    }
}
