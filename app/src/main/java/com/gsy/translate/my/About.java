package com.gsy.translate.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsy.translate.BaseActivity;
import com.gsy.translate.R;

/**
 * Created by Think on 2018/1/26.
 */

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView title = findViewById(R.id.titlebar_center);
        ImageView titleLeft = findViewById(R.id.titlebar_left);
        title.setText("关于");
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                About.this.finish();
            }
        });
    }
}
