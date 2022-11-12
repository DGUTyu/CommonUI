package com.wxdgut.commonui.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.commonui.R;

import java.text.SimpleDateFormat;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        switch (view.getId()) {
            case R.id.tv_welcome:
                String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                Log.e(BuildConfig.LOG_TAG, date + " onClick");
                TestLibrary.test("测试TestActivity");
                break;
        }
    }
}