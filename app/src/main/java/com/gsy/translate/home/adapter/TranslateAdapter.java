package com.gsy.translate.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsy.translate.R;
import com.gsy.translate.bean.DBWord;
import com.gsy.translate.utils.Tools;

import java.util.List;

/**
 * Created by Think on 2018/2/4.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.MyViewHodler> implements View.OnClickListener {
    public List<DBWord> data;
    Context context;
    private OnItemClickListener mOnItemClickListener = null;
    int padding;

    public TranslateAdapter(Context context,List<DBWord> data){
        this.context = context;
        this.data = data;
        padding = Tools.dp2px(context,10);
    }
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHodler hodler;
        TextView tv = new TextView(context);
        tv.setPadding(padding,padding,padding,padding);
        tv.setLines(1);
        hodler = new MyViewHodler(tv);
        hodler.itemView.setOnClickListener(this);
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        String str = data.get(position).getWord()+"    "+data.get(position).getDefinition();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, data.get(position).getWord().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new RelativeSizeSpan(1.2F), 0, data.get(position).getWord().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tv.setText(style);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        else
            return data.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if(mOnItemClickListener!=null)
            mOnItemClickListener.onItemClick(v,position);
    }

    class MyViewHodler extends RecyclerView.ViewHolder{
        TextView tv;
        public MyViewHodler(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}
