package com.gsy.translate.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Think on 2017/12/5.
 */

public abstract class AbstractPagerAdapter<T> extends PagerAdapter {
    public Context context;
    public ArrayList<T> data;
    boolean rollBack;
    boolean autoRoll;
    public AbstractPagerAdapter(Context context, ArrayList<T> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount(){
        if(rollBack){
            return Integer.MAX_VALUE;
        }else{
            return  data.size();
        }
    }
    public int getRealCount(){
        return data.size();
    }
    public int getRealPosition(int position){
        if(rollBack){
            return position%data.size();
        }else{
            return  position;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setRollBack(boolean rollBack) {
        if(data.size()>3)
            this.rollBack = rollBack;
    }


}
