package com.gsy.translate.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Think on 2018/1/28.
 */

public class LoadingProgressBar extends ProgressBar {
    Context context;
    private long finishInterval = 5000;
    private long flushInterval = 200;
    private long flushIntervalSec = 100;
    private long cycleIntervalSec = 500;
    Handler handler;
    Handler handlerSec;
    LoadingProgressBar progressBar = LoadingProgressBar.this;
    int increment;
    int incrementSec;
    public LoadingProgressBar(Context context) {
        super(context);
        init();
    }

    public LoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setMax(100);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    int progress = progressBar.getProgress()+increment;
                    if(progress>progressBar.getMax())
                        progress = progressBar.getMax();
                    progressBar.setProgress(progress);
                    if(progressBar.getProgress()!=progressBar.getMax())
                        sendEmptyMessageDelayed(0,flushInterval);
                    else
                        progressBar.setGone();
                }
            }
        };
        final int maxDistance = progressBar.getMax()/5;
        handlerSec = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    int progress = progressBar.getSecondaryProgress()+incrementSec;
                    if(progress>progressBar.getMax()||progress>progressBar.getProgress()+maxDistance){
                        progress = progressBar.getProgress();
                    }
                    progressBar.setSecondaryProgress(progress);
                    if(progressBar.getProgress()!=progressBar.getMax())
                        sendEmptyMessageDelayed(0,flushIntervalSec);
                }

            }
        };
    }

    private void setGone(){
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressBar.setVisibility(GONE);
            }
        }.sendEmptyMessageDelayed(0,500);
    }

    public void startLoading(){
        increment = (int)(flushInterval*(progressBar.getMax()-progressBar.getProgress())/finishInterval);
        incrementSec = (int) (flushIntervalSec*progressBar.getMax()/5/cycleIntervalSec);
        handler.sendEmptyMessage(0);
        handlerSec.sendEmptyMessage(0);
        handler.sendEmptyMessageDelayed(1,finishInterval*3/4);
    }

    public void pauseLoading(){
        handler.sendEmptyMessageDelayed(1,flushInterval);
    }

    public void setFinish(){
        finishInterval = 500;
        increment = (int)(flushInterval*(progressBar.getMax()-progressBar.getProgress())/finishInterval);
        incrementSec = (int) (flushIntervalSec*progressBar.getMax()/5/cycleIntervalSec);
    }

}
