package com.gsy.translate.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Think on 2017/12/21.
 */

public class MyDataBindingAdapter {

    @BindingAdapter("imageUrl")
    public static void loadImageFromUrl(ImageView view, String url){
        Picasso.with(view.getContext()).load(Constants.baseUrl+url).into(view);
    }
}
