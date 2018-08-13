package com.gsy.translate.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gsy.translate.R;
import com.gsy.translate.utils.Tools;

/**
 * Created by gsy on 2017/12/5.
 */

public class CustomViewPager extends FrameLayout implements ViewPager.OnPageChangeListener{
    Context context;
    ViewPager vp;
    LinearLayout ll_dots;
    View[] dots;
    AbstractPagerAdapter adapter;
    public CustomViewPager(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        initView();
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        this.context = context;
        initView();
    }
    public void initView(){
        vp = new ViewPager(context);
        LayoutParams vpLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(vp,vpLp);
        ll_dots = new LinearLayout(context);
        LayoutParams llLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        llLp.bottomMargin = Tools.dp2px(context,5);
        addView(ll_dots,llLp);
    }
    public void setAdapter(AbstractPagerAdapter adapter){
        this.adapter = adapter;
        vp.setAdapter(adapter);
        addDot();
        vp.addOnPageChangeListener(this);

    }

    private void addDot(){
        ll_dots.removeAllViews();
        if(adapter.data.size()<=1){
            return;
        }else {
            dots = new View[adapter.data.size()];
            for(int i=0;i<adapter.data.size();i++){
                ll_dots.setVisibility(View.VISIBLE);
                dots[i] = new View(context);
                android.widget.LinearLayout.LayoutParams lParams = new android.widget.LinearLayout.LayoutParams(Tools.dp2px(context,8),Tools.dp2px(context,8));
                if(i!=0){
                    lParams.leftMargin = Tools.dp2px(context,10);
                }
                dots[i].setLayoutParams(lParams);
                dots[i].setBackgroundResource(R.drawable.dot_selector);
                ll_dots.addView(dots[i]);
            }
            for(int j=0;j<dots.length;j++){
                dots[j].setEnabled(j==vp.getCurrentItem()%adapter.data.size());
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int j=0;j<dots.length;j++){
            dots[j].setEnabled(j==vp.getCurrentItem()%adapter.data.size());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
