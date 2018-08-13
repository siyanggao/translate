package com.gsy.translate.my;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.App;
import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;
import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.User;
import com.gsy.translate.databinding.SettingBinding;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.Tools;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Think on 2018/1/14.
 */

public class Setting extends BaseActivity {
    SettingBinding mBinding;
    User user = new User();
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private Bitmap bitmap;

    int selectedSex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting);
        initSharedPreferences();
        mBinding.titlebar.setTitle("设置");
        mBinding.titlebar.titlebarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting.this.finish();
            }
        });
        user.setAvatar(sharedPreferences.getString("avatar",null));
        user.setNickname(sharedPreferences.getString("nickname",null));
        user.setSex(sharedPreferences.getInt("sex",2));
        mBinding.setUser(user);
        if(user.getSex()==0)
            mBinding.settingSex.setText("保密");
        else if(user.getSex()==1)
            mBinding.settingSex.setText("男");
        else
            mBinding.settingSex.setText("女");
        Picasso.with(this).load(user.getAvatar()).into(mBinding.settingAvatar);
        mBinding.setPresenter(new Presenter());
    }

    public class Presenter{
        public void avatarDialog(View v){
            View view = getLayoutInflater().inflate(R.layout.dialog_checkphoto, null);

            TextView bt_camera=(TextView) view.findViewById(R.id.tv_dialog_takephoto);
            TextView bt_bulm=(TextView) view.findViewById(R.id.tv_dialog_checkfrombulm);
            TextView bt_cancle=(TextView) view.findViewById(R.id.tv_dialog_cancle);

            final Dialog dialog = new Dialog(Setting.this, R.style.transparentFrameWindowStyle);
            dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = dialog.getWindow();
            // 设置显示动画
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            // 设置显示位置
            dialog.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            bt_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    camera();
                }
            });
            bt_bulm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                    gallery();
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
        }

        public void nickName(View v){
            final EditText editText = new EditText(Setting.this);
            editText.setText(user.getNickname());
            new AlertDialog.Builder(Setting.this)
                    .setTitle("昵称")
                    .setView(editText)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String nickname = editText.getText().toString();
                            if(nickname==null&&"".equals(nickname)){
                                Toast.makeText(Setting.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                            }else{
                                CommonOkHttpClient.post(Constants.baseUrl+"/user/update")
                                        .addParam("nickname",nickname)
                                        .addParam("token",App.getToken())
                                        .addListener(new DisposeDataListener<BaseResponse>(){
                                            @Override
                                            public void onSuccess(BaseResponse responseObj) {
                                                if(responseObj.getCode()==1){
                                                    CustomToast.makeText(Setting.this,"更新成功").show();
                                                    user.setNickname(nickname);
                                                }else{
                                                    CustomToast.makeText(Setting.this,"更新失败").show();
                                                }
                                            }
                                        }).exec();
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        public void sex(View v){
            final int select;
            new AlertDialog.Builder(Setting.this)
                    .setTitle("性别")
                    .setSingleChoiceItems(new String[]{"保密", "男", "女"}, user.getSex(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedSex = which;
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            CommonOkHttpClient.post(Constants.baseUrl+"/user/update")
                                    .addParam("sex",selectedSex)
                                    .addParam("token",App.getToken())
                                    .addListener(new DisposeDataListener<BaseResponse>(){
                                        @Override
                                        public void onSuccess(BaseResponse responseObj) {
                                            if(responseObj.getCode()==1){
                                                CustomToast.makeText(Setting.this,"更新成功").show();
                                                user.setSex(selectedSex);
                                                if(user.getSex()==0)
                                                    mBinding.settingSex.setText("保密");
                                                else if(user.getSex()==1)
                                                    mBinding.settingSex.setText("男");
                                                else
                                                    mBinding.settingSex.setText("女");
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt("sex",user.getSex());
                                                editor.commit();
                                            }else{
                                                CustomToast.makeText(Setting.this,"更新失败").show();
                                            }
                                        }
                                    }).exec();
                        }

                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片...", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                FileOutputStream out=new FileOutputStream(new File(getApplicationContext().getFilesDir().getAbsolutePath()+"avatar.jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                upload();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void upload() {
        File f=new File(getApplicationContext().getFilesDir().getAbsolutePath()+"avatar.jpg");
        CommonOkHttpClient.multiPost(Constants.baseUrl+"/upload")
                .addParam("token", App.getToken())
                .addParam("type","avatar")
                .addParam("file",f)
                .addListener(new DisposeDataListener<String>(){
                    @Override
                    public void onSuccess(String responseObj) {
                        Log.i("tag",responseObj);
                        CommonOkHttpClient.post(Constants.baseUrl+"/user/update")
                                .addParam("token", App.getToken())
                                .addParam("avatar", Constants.baseUrl+responseObj)
                                .addListener(new DisposeDataListener<BaseResponse>(){
                                    @Override
                                    public void onSuccess(BaseResponse responseObj) {
                                        if(responseObj.getCode()==1) {
                                            mBinding.settingAvatar.setImageBitmap(bitmap);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("avatar",Constants.baseUrl+responseObj);
                                            editor.commit();
                                            Toast.makeText(Setting.this,"更新成功",Toast.LENGTH_SHORT).show();
                                        }else
                                            Toast.makeText(Setting.this,responseObj.getMsg(),Toast.LENGTH_SHORT).show();
                                    }
                                }).exec();
                    }

                    @Override
                    public void onFailure(Exception resonObj) {
                        Tools.l(resonObj.getMessage());
                    }
                }).exec();
    }

    /*
	 * 从相册获取
	 */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 144);
        intent.putExtra("outputY", 144);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
