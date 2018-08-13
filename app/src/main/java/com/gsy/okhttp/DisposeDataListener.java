package com.gsy.okhttp;

import java.lang.reflect.Type;

/**
 * Created by siyang on 2017/4/17.
 */

public class DisposeDataListener<T> {
    protected Type type;
    public DisposeDataListener(){
        type = ClassTypeReflect.getModelClazz(getClass());
    }
    public void onSuccess(T responseObj){};
    public void onFailure(Exception resonObj){};
}
