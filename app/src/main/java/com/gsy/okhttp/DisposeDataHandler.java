package com.gsy.okhttp;

/**
 * Created by gsy on 2017/1/22.
 */

public class DisposeDataHandler {
    public DisposeDataListener mListener = null;
    public String mSource = null;

    public DisposeDataHandler(DisposeDataListener listener){
        mListener = listener;
    }

    public DisposeDataHandler(DisposeDataListener listener, String source)
    {
        this.mListener = listener;
        this.mSource = source;
    }

}
