package com.gsy.translate.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsy.translate.R;
import com.gsy.translate.bean.Article;
import com.gsy.translate.home.ArticleDetail;
import com.gsy.translate.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Think on 2017/12/21.
 */

public class HomeArticleAdapter extends RecyclerView.Adapter<HomeArticleAdapter.MyViewHolder> implements View.OnClickListener{

    Context context;
    List<Article> data;

    public HomeArticleAdapter(Context context, List<Article> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        if(viewType==2){
            holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.article_item_big_image,parent,false));
        }else{
            holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.article_item,parent,false));

        }
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article itemData = data.get(position);
        holder.title.setText(itemData.getTitle());
        holder.from.setText(itemData.getOrigin());
        if(itemData.getImageSize()==2){
            Picasso.with(context).load(Constants.baseUrl+itemData.getImagePath()).into(holder.image);
        }else if(itemData.getImageSize()==1){
            Picasso.with(context).load(Constants.baseUrl+itemData.getImagePath()).into(holder.image);
            holder.contentBrief.setText(itemData.getContentBrief());
            holder.image.setVisibility(View.VISIBLE);
        }else{
            holder.contentBrief.setText(itemData.getContentBrief());
            holder.image.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getImageSize()==2){
            return 2;
        }else{
            return 1;
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Intent intent = new Intent(context, ArticleDetail.class);
        intent.putExtra("article",data.get(position));
        context.startActivity(intent);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView contentBrief;
        TextView from;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_item_title);
            contentBrief = itemView.findViewById(R.id.article_item_brief);
            from = itemView.findViewById(R.id.article_item_from);
            image = itemView.findViewById(R.id.article_item_image);
        }
    }
}
