package com.wxdgut.uilibrary.test;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wxdgut.uilibrary.BuildConfig;
import com.wxdgut.uilibrary.R;

import java.text.SimpleDateFormat;

public class TestUIActivity extends AppCompatActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ui);
        initView();
        initEvent();
    }

    //初始化视图控件
    private void initView() {
        tv_welcome = findViewById(R.id.tv_welcome);
    }

    //初始化事件
    private void initEvent() {
        tv_welcome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) { //资源ID不能在安卓库模块的switch语句中使用
//            case R.id.tv_welcome:
//                break;
//        }
        if (view.getId() == R.id.tv_welcome) {
            String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            Log.e(BuildConfig.LOG_TAG, date + " onClick");
            TestUILibrary.test(TestUIActivity.this, getString(R.string.libName));
        }
    }
}