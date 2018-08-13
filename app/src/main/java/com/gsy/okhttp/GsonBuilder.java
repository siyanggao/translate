package com.gsy.okhttp;

import com.google.gson.Gson;

/**
 * Created by gsy on 2017/1/22.
 */

public class GsonBuilder {

    private static Gson gson;

    public static Gson getInstance(){
        if(gson==null){
            gson = new Gson();
        }
        return gson;
    }
}
