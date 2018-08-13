package com.gsy.translate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.gsy.translate.R;


/**
 * Created by gsy on 2016/9/19.
 */
public class NoNetworkPop extends PopupWindow {
    RefreshClick refreshClick;
    public NoNetworkPop(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.no_network,null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.refresh);
        imageView.setOnClickListener(new RefreshClickListener());
    }
    class RefreshClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            NoNetworkPop.this.dismiss();
            refreshClick.refreshClick();
        }
    }
    public void setRefreshClick(RefreshClick refreshClick){
        this.refreshClick = refreshClick;
    }
    public interface RefreshClick{
        public void refreshClick();
    }
}
