package com.gsy.translate.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.R;
import com.gsy.translate.bean.Article;
import com.gsy.translate.home.WordTranslateFragment;
import com.gsy.translate.home.adapter.HomeArticleAdapter;
import com.gsy.translate.home.bean.HomeArticleBean;
import com.gsy.translate.home.bean.HomeBean;
import com.gsy.translate.databinding.FragmentHomeBinding;
import com.gsy.translate.home.adapter.HomeVpAdapter;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.Tools;
import com.gsy.translate.view.DividerItemDecoration;
import com.gsy.translate.view.FullyLinearLayoutManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Think on 2017/12/5.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FragmentHomeBinding mBinding;
    int page = 1;
    //boolean refreshOrMore = true;
    HomeArticleAdapter articleAdapter;
    List<Article> articleData;
    FullyLinearLayoutManager mLinerLayoutManager;
    SharedPreferences sharedPreferences;
    WordTranslateFragment wordTranslateFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        initView();
        sharedPreferences = getActivity().getSharedPreferences("myconfig",Context.MODE_PRIVATE);
        getSentenceWord();
        getArticle(true);
        return mBinding.getRoot();
    }

    void getSentenceWord(){
        int sentenceWordUpdateTime = sharedPreferences.getInt("SentenceWordUpdateTime",0);
        Calendar sentenceWordUpdateDate = Calendar.getInstance();
        sentenceWordUpdateDate.setTimeInMillis(sentenceWordUpdateTime*1000);
        Calendar now = Calendar.getInstance();
        if(now.get(Calendar.DATE)==sentenceWordUpdateDate.get(Calendar.DATE)){
            String dataJson = sharedPreferences.getString("SentenceWordData",null);
            HomeBean data = new Gson().fromJson(dataJson,HomeBean.class);
            initSentenceWord(data);
            return;
        }
        CommonOkHttpClient.post(Constants.baseUrl+"/home/sentenceword")
                .addListener(new DisposeDataListener<HomeBean>(){
                    @Override
                    public void onSuccess(HomeBean responseObj) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("SentenceWordUpdateTime",(int)System.currentTimeMillis()/1000);
                        editor.putString("SentenceWordData",new Gson().toJson(responseObj));
                        editor.commit();
                        initSentenceWord(responseObj);
                    }
                    @Override
                    public void onFailure(Exception resonObj) {
                        //Snackbar.make(rootView,R.string.net_failure,Snackbar.LENGTH_SHORT).show();
                        CustomToast.makeText(getActivity(),R.string.net_failure,Toast.LENGTH_SHORT).show();
                    }
                }).exec();
    }

    void initSentenceWord(HomeBean data){
        HomeVpAdapter vpAdapter = new HomeVpAdapter(getActivity(),data.getData().getSentence());
        vpAdapter.setRollBack(true);
        mBinding.vpHead.setAdapter(vpAdapter);
        mBinding.setWord(data.getData().getWord().get(0));
        mBinding.tvDate.setText(Tools.formatDate(new Date(),"yyyy/MM/dd"));

    }

    void getArticle(final boolean refreshOrMore){
        CommonOkHttpClient.post(Constants.baseUrl+"/home/article")
                .addParam("currentPage",page)
                .addParam("pageSize",20)
                .addListener(new DisposeDataListener<HomeArticleBean>(){
                    @Override
                    public void onSuccess(HomeArticleBean responseObj) {
                        if(refreshOrMore){
                            articleData = responseObj.getData();
                            articleAdapter = new HomeArticleAdapter(getActivity(),articleData);
                            mBinding.rvArticle.setAdapter(articleAdapter);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("articleData",new Gson().toJson(responseObj));
                            editor.commit();
                        }else{
                            if(responseObj.getData()==null||responseObj.getData().size()==0){
                                Toast.makeText(getActivity(),R.string.no_more,Toast.LENGTH_SHORT).show();
                            }else{
                                articleData.addAll(responseObj.getData());
                                articleAdapter.notifyDataSetChanged();
                            }
                        }
                        page++;
                        mBinding.homeSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Exception resonObj) {
                        String articleStr = sharedPreferences.getString("articleData",null);
                        if(articleStr!=null){
                            articleAdapter = new HomeArticleAdapter(getActivity(),articleData);
                            mBinding.rvArticle.setAdapter(articleAdapter);
                        }
                        mBinding.homeSwipe.setRefreshing(false);
                        CustomToast.makeText(getActivity(),R.string.net_failure,Toast.LENGTH_SHORT).show();
                    }
                }).exec();
    }

    void initView(){
        mLinerLayoutManager = new FullyLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mBinding.rvArticle.setLayoutManager(mLinerLayoutManager);
        mBinding.rvArticle.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        mBinding.homeSwipe.setOnRefreshListener(this);
        mBinding.rvArticle.setNestedScrollingEnabled(false);
        mBinding.homeSv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==0){
                    mBinding.homeFb.setVisibility(View.GONE);
                }else if(scrollY<255){
                    if(mBinding.homeFb.getVisibility()==View.GONE) mBinding.homeFb.setVisibility(View.VISIBLE);
                    mBinding.homeFb.getBackground().setAlpha(scrollY);
                }else if(mBinding.homeSv.getChildAt(0).getHeight() - mBinding.homeSv.getHeight() == mBinding.homeSv.getScrollY()){
                    mBinding.homeSwipe.setRefreshing(true);
                    getArticle(false);
                }
            }
        });
        mBinding.setPresenter(new Presenter());
    }



    @Override
    public void onRefresh() {
        page = 1;
        getArticle(true);
        getSentenceWord();
    }

    public class Presenter{
        public void search(View v){
            //mBinding.translateContainer.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            if(wordTranslateFragment==null)
                wordTranslateFragment = new WordTranslateFragment();
            wordTranslateFragment.show(fragmentManager,"wordTranslateFragment");
        }
    }

}
