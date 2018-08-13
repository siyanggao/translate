package com.gsy.translate.fragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.App;
import com.gsy.translate.R;
import com.gsy.translate.bean.Version;
import com.gsy.translate.databinding.FragmentMyBinding;
import com.gsy.translate.my.About;
import com.gsy.translate.my.DownloadReceiver;
import com.gsy.translate.my.FeedBack;
import com.gsy.translate.my.Login;
import com.gsy.translate.my.Setting;
import com.gsy.translate.my.bean.VersionBean;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.share.Share;
import com.squareup.picasso.Picasso;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Think on 2017/12/5.
 */

public class MyFragment extends Fragment {

    FragmentMyBinding mBinding;
    SharedPreferences sharedPreferences;
    DownloadReceiver downloadReceiver;
    public Share share;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my, container,false);
        sharedPreferences = getActivity().getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        initView();
        initCheckUpdate();
        return mBinding.getRoot();
    }

    private void initCheckUpdate() {
        int checkedVersionCode = sharedPreferences.getInt("versionCode",0);
        String checkedVersionName = sharedPreferences.getString("versionName",null);
        if(checkedVersionCode!=0){
            Version currentVersion = getCurrentVersion();
            if(currentVersion!=null){
                if(currentVersion.getVersionCode()<checkedVersionCode){
                    mBinding.checkUpdateDot.setVisibility(View.VISIBLE);
                    mBinding.checkUpdateTv.setText("发现新版本："+checkedVersionName);
                }
            }
        }
    }

    private Version getCurrentVersion(){
        Version version = new Version();
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
            version.setVersionCode(packageInfo.versionCode);
            version.setVersionName(packageInfo.versionName);
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    void initView(){
        mBinding.setPresenter(new Presenter());
        initViewByLoginStatus();
    }

    void initViewByLoginStatus(){
        if(App.getToken()==null){
            mBinding.llNotLogin.setVisibility(View.VISIBLE);
            mBinding.llHasLogin.setVisibility(View.GONE);
            mBinding.myLogout.setVisibility(View.GONE);
        }else{
            mBinding.llNotLogin.setVisibility(View.GONE);
            mBinding.llHasLogin.setVisibility(View.VISIBLE);
            mBinding.myLogout.setVisibility(View.VISIBLE);
            String nickname = sharedPreferences.getString("nickname",null);
            String avatar = sharedPreferences.getString("avatar",null);
            mBinding.myNickname.setText(nickname);
            Picasso.with(getActivity()).load(avatar).into(mBinding.myAvatar);
        }

    }

    public class Presenter{
        public void login(View v){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivityForResult(intent,1);
        }
        public void logout(View v){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            initViewByLoginStatus();
        }
        public void setting(View v){
            if(App.getToken()==null){
                login(null);
            }else{
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivityForResult(intent,1);
            }
        }

        public void checkVersion(View v){
            CommonOkHttpClient.post(Constants.baseUrl+"/appVersion")
                    .addListener(new DisposeDataListener<VersionBean>(){
                        @Override
                        public void onSuccess(final VersionBean responseObj) {
                            if(responseObj.getCode()==1){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("versionCode",responseObj.getVersion().getVersionCode());
                                editor.putString("versionName",responseObj.getVersion().getVersionName());
                                editor.commit();
                                Version currentVersion = getCurrentVersion();
                                if(currentVersion!=null){
                                    if(currentVersion.getVersionCode()<responseObj.getVersion().getVersionCode()){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("发现新版本，是否升级？新版本："+responseObj.getVersion().getVersionName());
                                        builder.setTitle("检查更新");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                downloadApp(responseObj.getVersion().getAppPath());
                                            }
                                        });
                                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("已经是最新版本");
                                        builder.setTitle("检查更新");
                                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                }else{
                                    CustomToast.makeText(getActivity(),"获取本地版本失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }).exec();
        }
        public void feedback(View v){
            Intent intent = new Intent(getActivity(), FeedBack.class);
            startActivity(intent);
        }

        public void share(View v){
            if(share==null)
                share = new Share(getActivity(),"http://www.baidu.com","翻译","翻译助手","http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
            share.share();
        }

        public void about(View v){
            Intent intent = new Intent(getActivity(), About.class);
            startActivity(intent);
        }

    }
    private void downloadApp(String appPath) {
        if(downloadReceiver==null) downloadReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(downloadReceiver,intentFilter);
        DownloadManager downloadManager = (DownloadManager)getActivity().getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Constants.baseUrl+appPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "translate.apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            initViewByLoginStatus();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(downloadReceiver!=null) getActivity().unregisterReceiver(downloadReceiver);
    }
}
