package com.gsy.translate.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsy.translate.R;
import com.gsy.translate.bean.Sentence;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.view.AbstractPagerAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Think on 2017/12/9.
 */

public class HomeVpAdapter extends AbstractPagerAdapter {
    ArrayList<Sentence> data;
    ArrayList<View> views;
    public HomeVpAdapter(Context context, ArrayList<Sentence> data) {
        super(context, data);
        this.data = data;
        initView();
    }
    public void initView(){
        views = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            FrameLayout rooView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.home_vp_item,null);
            ImageView iv = rooView.findViewById(R.id.home_vp_item_iv);
            TextView tv = rooView.findViewById(R.id.homt_vp_item_tv);
            Picasso.with(context).load(Constants.baseUrl+data.get(i).getImagePath()).into(iv);
            tv.setText(data.get(i).geteContent()+'\n'+data.get(i).getcContent());
            views.add(rooView);
        }

    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(getRealPosition(position)));

        return views.get(getRealPosition(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(views.get(getRealPosition(position)));
    }

}
