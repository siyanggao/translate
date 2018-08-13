package com.gsy.translate.message;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.App;
import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;
import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.databinding.PublishBinding;
import com.gsy.translate.my.Login;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;

/**
 * Created by Think on 2018/2/3.
 */

public class Publish extends BaseActivity {
    PublishBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.publish);
        mBinding.setPresenter(new Presenter());
        initView();
    }

    private void initView() {
        mBinding.titlebar.setTitle("留言墙");
        mBinding.titlebar.titlebarRight3.setVisibility(View.VISIBLE);
        mBinding.titlebar.titlebarRight3.setText("发表");
        mBinding.titlebar.titlebarRight3.setTextColor(Color.BLACK);
        mBinding.titlebar.titlebarRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.getPresenter().publish(mBinding.titlebar.titlebarRight3);
            }
        });
    }

    public class Presenter{
        public void publish(View v){
            if(App.getToken()==null){
                CustomToast.makeText(Publish.this,"请登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Publish.this, Login.class);
                startActivity(intent);
                return;
            }
            if(TextUtils.isEmpty(mBinding.content.getText())){
                CustomToast.makeText(Publish.this,"请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
            CommonOkHttpClient.post(Constants.baseUrl+"/message/publish")
                    .addParam("token",App.getToken())
                    .addParam("content",mBinding.content.getText().toString())
                    .addListener(new DisposeDataListener<BaseResponse>(){
                        @Override
                        public void onSuccess(BaseResponse responseObj) {
                            if(responseObj.getCode()==1){
                                CustomToast.makeText(Publish.this,"发表成功", Toast.LENGTH_SHORT).show();
                                Publish.this.finish();
                            }else if(responseObj.getCode()==2){
                                CustomToast.makeText(Publish.this,"请登录", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Publish.this, Login.class);
                                startActivity(intent);
                            }else{
                                CustomToast.makeText(Publish.this,responseObj.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception resonObj) {
                            CustomToast.makeText(Publish.this,resonObj.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).exec();

        }
        public void clear(View v){
            mBinding.content.setText(null);
        }
        public void translate(View v){

        }
        public void translateClear(View v){
            mBinding.translateContent.setText(null);
        }
    }
}
