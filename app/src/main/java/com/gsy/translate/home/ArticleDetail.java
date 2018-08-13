package com.gsy.translate.home;

import android.app.FragmentManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;
import com.gsy.translate.bean.Article;
import com.gsy.translate.databinding.ArticleDetailBinding;
import com.gsy.translate.home.bean.ArticleDetailBean;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.Tools;
import com.gsy.translate.utils.share.Share;
import com.squareup.picasso.Picasso;

/**
 * Created by Think on 2018/1/28.
 */

public class ArticleDetail extends BaseActivity {
    ArticleDetailBinding mBinding;
    Article article;
    Share share;
    WordTranslateFragment wordTranslateFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.article_detail);
        article = (Article) getIntent().getSerializableExtra("article");
        mBinding.title.setTitle(article.getTitle());
        mBinding.title.titlebarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetail.this.finish();
            }
        });

        initView();
        loading();
        getData();

    }

    private void getData() {
        CommonOkHttpClient.post(Constants.baseUrl+"/home/articledetail")
                .addParam("id",article.getId())
                .addListener(new DisposeDataListener<ArticleDetailBean>(){
                    @Override
                    public void onSuccess(ArticleDetailBean responseObj) {
                        if(responseObj.getCode()==1){
                            article.setContent(responseObj.getData().getContent());
                        }else{
                            CustomToast.makeText(ArticleDetail.this,responseObj.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        mBinding.title.titlebarProgressbar.setFinish();
                    }

                    @Override
                    public void onFailure(Exception resonObj) {
                        CustomToast.makeText(ArticleDetail.this,R.string.net_failure, Toast.LENGTH_SHORT).show();
                        mBinding.title.titlebarProgressbar.setFinish();
                    }
                }).exec();
    }

    private void initView() {
        mBinding.setPresenter(new Presenter());
        mBinding.setArticle(article);
        if(article.getImageSize()==2){
            Picasso.with(this).load(article.getImagePath()).into(mBinding.img);
        }
    }

    void loading(){
        mBinding.title.titlebarProgressbar.startLoading();
    }

    public class Presenter{
        public void share(View v){
            if(share==null) share = new Share(ArticleDetail.this,Constants.IndexUrl,article.getTitle(),article.getContentBrief(),article.getImagePath()==null?Constants.Icon:article.getImagePath());
            share.share();
        }

        public void translate(View v){
            FragmentManager fragmentManager = getFragmentManager();
            if(wordTranslateFragment==null)
                wordTranslateFragment = new WordTranslateFragment();
            wordTranslateFragment.show(fragmentManager,"wordTranslateFragment");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(share!=null)
            share.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(share!=null)
            share.onNewIntent(intent);
    }
}
