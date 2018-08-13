package com.gsy.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gsy on 2017/1/22.
 */

public class CommonJsonCallBack<ResultType> implements Callback {

    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR = -2;
    protected final int OTHER_ERROR = -3;

    private DisposeDataListener<ResultType> mListener;
    private Handler mDelieverHandler;
    Type type;
    public CommonJsonCallBack (DisposeDataHandler handler){
        mListener = handler.mListener;
        mDelieverHandler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void onFailure(Call call, final IOException e) {
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(e);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.code()>=400){
            handleException(new OkHttpException(response.code(),"网络请求错误"));
            return;
        }
        final String result = response.body().string();
        if(result==null||result==""){
            handleException(new OkHttpException(NETWORK_ERROR,"返回数据为空"));
            return;
        }
        try {
            ResultType obj;
            if(mListener.type==String.class)
                obj = (ResultType) result;
            else
                obj = GsonBuilder.getInstance().fromJson(result,mListener.type);
            if(obj==null){
                handleException(new OkHttpException(JSON_ERROR,"json解析失败"));
            }else{
                handleResqonse(obj);
            }
        }catch (Exception e){
            handleException(new OkHttpException(OTHER_ERROR,e.getMessage()));
        }
    }

    private void handleResqonse(final ResultType result){
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                /*Class<? extends Object> classType = result.getClass();
                Field[] fields = classType.getDeclaredFields();
                for(Field field : fields){
                    String fieldName = field.getName();
                    if(fieldName.equals("resultCode")){
                        String strLetter = fieldName.substring(0,1).toUpperCase();
                        String getName = "get" + strLetter + fieldName.substring(1);
                        Method getMethod = null;
                        try {
                            getMethod = classType.getMethod(getName, new Class[] {});
                            Object methodReturn = getMethod.invoke(result, new Object[] {});
                            int value = Integer.valueOf(methodReturn == null ? "" : methodReturn.toString());
                            if(value==0){//处理错误提醒

                            }else{

                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                }*/
                mListener.onSuccess(result);
            }
        });
    }
    private void handleException(final OkHttpException e){
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(e);
            }
        });
    }
}
