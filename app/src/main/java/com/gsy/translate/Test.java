package com.gsy.translate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.bean.Message;
import com.gsy.translate.message.adapter.MessageAdapter;
import com.gsy.translate.message.bean.MessageBean;
import com.gsy.translate.utils.Constants;

import java.util.List;

/**
 * Created by Think on 2018/2/1.
 */

public class Test extends AppCompatActivity {
    MessageAdapter adapter;
    List<Message> data;
    int page = 1;
    RecyclerView rvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvMessage = findViewById(R.id.rv_message);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向：纵向
        rvMessage.setLayoutManager(manager);
        getData(true);
    }
    private void getData(final boolean refreshOrMore) {
        CommonOkHttpClient.post(Constants.baseUrl+"/message/list")
                .addParam("currentPage",page)
                .addParam("token", App.getToken())
                .addListener(new DisposeDataListener<MessageBean>(){
                    @Override
                    public void onSuccess(MessageBean responseObj) {
                        if(responseObj.getCode()==1){
                            if(refreshOrMore){
                                data = responseObj.getData();
                                adapter = new MessageAdapter(Test.this,data);
                                rvMessage.setAdapter(adapter);
                            }else{
                                data.addAll(responseObj.getData());
                                adapter.notifyDataSetChanged();
                            }
                            //mBinding.swipeRefresh.setRefreshing(false);
                        }else{

                        }
                    }
                }).exec();
    }
}
