package com.gsy.translate.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.App;
import com.gsy.translate.R;
import com.gsy.translate.bean.Message;
import com.gsy.translate.databinding.FragmentMessageBinding;
import com.gsy.translate.message.Publish;
import com.gsy.translate.message.adapter.MessageAdapter;
import com.gsy.translate.message.bean.MessageBean;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.Tools;
import com.gsy.translate.view.DividerItemDecoration;

import java.util.List;

/**
 * Created by Think on 2017/12/5.
 */

public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FragmentMessageBinding mBinding;
    MessageAdapter adapter;
    List<Message> data;
    int page = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message,container,false);
        initView();
        getData(true);

        return mBinding.getRoot();
    }

    private void getData(final boolean refreshOrMore) {
        if(refreshOrMore) page = 1;
        else page++;
        CommonOkHttpClient.post(Constants.baseUrl+"/message/list")
                .addParam("currentPage",page)
                .addParam("token", App.getToken())
                .addListener(new DisposeDataListener<MessageBean>(){
                    @Override
                    public void onSuccess(MessageBean responseObj) {
                        if(responseObj.getCode()==1){
                            if(refreshOrMore){
                                data = responseObj.getData();
                                adapter = new MessageAdapter(getActivity(),data);
                                mBinding.rvMessage.setAdapter(adapter);
                            }else{
                                data.addAll(responseObj.getData());
                                adapter.notifyDataSetChanged();
                            }

                        }else{

                        }
                        mBinding.swipeRefresh.setRefreshing(false);
                    }
                }).exec();
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    void initView(){
        mBinding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mBinding.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_launcher);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END;
        lp.setMarginEnd(Tools.dp2px(getActivity(),10));
        mBinding.toolbar.addView(imageView,lp);
        mBinding.swipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rvMessage.setLayoutManager(manager);
        mBinding.rvMessage.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        mBinding.rvMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()){
                        mBinding.swipeRefresh.setRefreshing(true);
                        getData(false);
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Publish.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
