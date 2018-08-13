package com.gsy.translate.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsy.translate.R;
import com.gsy.translate.bean.Message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Think on 2018/1/31.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHodler> implements View.OnClickListener{
    public List<Message> data;
    Context context;

    public MessageAdapter(Context context,List<Message> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHodler viewHodler;
        if(viewType==0){
            viewHodler = new MyViewHodler(LayoutInflater.from(context).inflate(R.layout.message_item,parent,false));
        }else{
            viewHodler = new MyViewHodler(LayoutInflater.from(context).inflate(R.layout.message_item,parent,false));
        }

        return viewHodler;
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        Message msg = data.get(position);
        holder.name.setText(msg.getUsername());
        holder.content.setText(msg.getContent());
        holder.time.setText(msg.getGmtCreate().toString());
        Picasso.with(context).load(msg.getAvatar()).into(holder.avatar);
        if(!msg.isUp()){
            holder.up.setImageResource(R.mipmap.unup);
            holder.up.setOnClickListener(this);
        }else{
            holder.up.setImageResource(R.mipmap.up);
            holder.up.setOnClickListener(null);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTime(msg.getGmtCreate());
        if(now.get(Calendar.YEAR)==time.get(Calendar.YEAR)){
            if(now.get(Calendar.MONTH)==time.get(Calendar.MONTH)){
                if(now.get(Calendar.DATE)==time.get(Calendar.DATE)){
                    holder.time.setText("今天 "+sdf2.format(time.getTimeInMillis()));
                }else if (now.get(Calendar.DATE)==time.get(Calendar.DATE)-1) {
                    holder.time.setText("昨天 "+sdf2.format(time.getTimeInMillis()));
                }else {
                    holder.time.setText(sdf1.format(time.getTimeInMillis()));
                }
            }else {
                holder.time.setText(sdf1.format(time.getTimeInMillis()));
            }
        }else {
            holder.time.setText(sdf1.format(time.getTimeInMillis()));
        }
    }

    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        else
            return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).isOwn())
            return 1;
        else
            return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_item_up:

                break;
        }
    }

    class MyViewHodler extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name;
        ImageView gender;
        TextView content;
        TextView time;
        ImageView up;
        public MyViewHodler(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.message_item_avatar);
            name = itemView.findViewById(R.id.message_item_name);
            gender = itemView.findViewById(R.id.message_item_gender);
            content = itemView.findViewById(R.id.message_item_content);
            time = itemView.findViewById(R.id.message_item_time);
            up = itemView.findViewById(R.id.message_item_up);
        }
    }
}
