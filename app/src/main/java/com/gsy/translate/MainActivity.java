package com.gsy.translate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsy.translate.fragment.HomeFragment;
import com.gsy.translate.fragment.MessageFragment;
import com.gsy.translate.fragment.MyFragment;
import com.gsy.translate.fragment.TranslateFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public HomeFragment home;//首页
    public TranslateFragment translate;//翻译
    public MessageFragment message;//消息
    public MyFragment my;//我的
    public ImageView[] bt_menu = new ImageView[4];//底部菜单
    public TextView[] tv_menu = new TextView[4];//底部textview
    private LinearLayout[] bt_big_menu = new LinearLayout[4];
    private int[] bt_big_menu_id = {R.id.ll_menu_0,R.id.ll_menu_1,R.id.ll_menu_2,R.id.ll_menu_3};
    private int[] bt_menu_id = {R.id.iv_menu_0,R.id.iv_menu_1,R.id.iv_menu_2,R.id.iv_menu_3};
    private int[] tv_menu_id = {R.id.tv_menu_0,R.id.tv_menu_1,R.id.tv_menu_2,R.id.tv_menu_3};
    public int[] select_on = {R.mipmap.home_select,R.mipmap.my_select,R.mipmap.message_select,R.mipmap.my_select};
    public int[] select_off = {R.mipmap.home_unselect,R.mipmap.my_unselect,R.mipmap.message_unselect,R.mipmap.my_unselect};
    private int last_select_bt = 0;
    private int on_color = Color.parseColor("#18d7b1");
    private int off_color = Color.parseColor("#666666");
    public LinearLayout menu_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            android.app.FragmentManager fm = getFragmentManager();
            home = (HomeFragment) fm.findFragmentByTag("home");
            translate = (TranslateFragment) fm.findFragmentByTag("group");
            message = (MessageFragment) fm.findFragmentByTag("message");
            my = (MyFragment) fm.findFragmentByTag("my");
        }
        initView();
//Intent intent = new Intent(this,Test.class);
//startActivity(intent);
//        bt_big_menu[2].callOnClick();
    }


    private void initView(){
        menu_button = (LinearLayout) findViewById(R.id.menu_bottom);
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
            tv_menu[i] = (TextView) findViewById(tv_menu_id[i]);
            bt_big_menu[i] = (LinearLayout) findViewById(bt_big_menu_id[i]);
            bt_big_menu[i].setOnClickListener(this);
        }
        //初始化首页
        if (home == null) {
            home = new HomeFragment();
            addFragment(home,"home");
            showFragment(home);
        }else {
            showFragment(home);
        }
        if(translate!=null) hideFragment(translate);
        if(message!=null) hideFragment(message);
        if(my!=null) hideFragment(my);
        bt_menu[0].setImageResource(select_on[0]);
        tv_menu[0].setTextColor(on_color);
    }

    @Override
    public void onClick(View view) {
        switch (last_select_bt){
            case 0:
                hideFragment(home);
                break;
            case 1:
                hideFragment(translate);
                break;
            case 2:
                hideFragment(message);
                break;
            case 3:
                hideFragment(my);
                break;
        }
        bt_menu[last_select_bt].setImageResource(select_off[last_select_bt]);
        tv_menu[last_select_bt].setTextColor(off_color);
        switch (view.getId()) {
            case R.id.ll_menu_0 :
                if (home == null) {//首页
                    home = new HomeFragment();
                    addFragment(home,"home");
                    showFragment(home);
                }else {
                    showFragment(home);
                }
                last_select_bt = 0;
                break;

            case R.id.ll_menu_1:
                if (translate == null) {//翻译
                    translate = new TranslateFragment();
                    addFragment(translate,"translate");
                    showFragment(translate);
                }else  {
                    showFragment(translate);
                }
                last_select_bt = 1;
                break;

            case R.id.ll_menu_2:
                if (message == null) {//消息
                    message = new MessageFragment();
                    addFragment(message,"message");
                    showFragment(message);
                }else  {
                    showFragment(message);
                }
                last_select_bt = 2;
                break;

            case R.id.ll_menu_3:
                if (my == null) {//我的
                    my = new MyFragment();
                    addFragment(my,"my");
                    showFragment(my);
                }else {
                    showFragment(my);
                }
                last_select_bt = 3;
                break;
            default:
                break;
        }
        bt_menu[last_select_bt].setImageResource(select_on[last_select_bt]);
        tv_menu[last_select_bt].setTextColor(on_color);
    }
    /**添加fragment*/
    public void addFragment(Fragment fragment, String tag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment,tag);
        ft.commit();
    }

    /**显示fragment*/
    public void showFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    /**隐藏fragment*/
    public void hideFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(my!=null&&my.share!=null)
            my.share.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(my!=null&&my.share!=null)
            my.share.onActivityResult(requestCode,resultCode,data);
    }
}