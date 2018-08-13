package com.gsy.translate.my;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;
import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.databinding.FeedbackBinding;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;

/**
 * Created by Think on 2018/1/24.
 */

public class FeedBack extends BaseActivity {
    FeedbackBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.feedback);
        mBinding.titlebar.setTitle("技术支持与反馈");
        mBinding.titlebar.titlebarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBack.this.finish();
            }
        });
        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mBinding.content.getText())||TextUtils.isEmpty(mBinding.contact.getText())){
                    CustomToast.makeText(FeedBack.this,"请填写意见及联系方式", Toast.LENGTH_SHORT).show();
                }else{
                    CommonOkHttpClient.post(Constants.baseUrl+"/feedback/add")
                            .addListener(new DisposeDataListener<BaseResponse>(){
                                @Override
                                public void onSuccess(BaseResponse responseObj) {
                                    if(responseObj.getCode()==1){
                                        CustomToast.makeText(FeedBack.this,"提交成功，感谢您宝贵的意见", Toast.LENGTH_SHORT).show();
                                        FeedBack.this.finish();
                                    }else{
                                        CustomToast.makeText(FeedBack.this,"提交失败", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Exception resonObj) {
                                    CustomToast.makeText(FeedBack.this,"提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }).exec();
                }
            }
        });
    }
}
