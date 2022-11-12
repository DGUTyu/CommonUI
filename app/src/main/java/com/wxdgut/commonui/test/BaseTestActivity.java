package com.wxdgut.commonui.test;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.commonui.R;

/**
 * FileName: BaseTestActivity
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile:
 */
public class BaseTestActivity extends AppCompatActivity {
    //避免后期写ExampleActivity.this等内容（查看该类的使用情况时能减少干扰）
    protected BaseTestActivity baseContext;
    //日志标志,不能直接写死TAG = BaseTestActivity.class.getCanonicalName();
    protected String TAG;
    //Toast
    protected Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        baseContext = this;
        TAG = this.getClass().getCanonicalName();
        if (TAG == null) TAG = BuildConfig.LOG_TAG;
        initToast(this);
    }

    //初始化Toast
    public void initToast(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    //Toast
    public void toast(String content) {
        toast.setText(content);
        toast.show();
    }

    //打印日志 注意区分引用的是哪个BuildConfig
    public void i(String text) {
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.i(TAG, text);
            }
        }
    }

    //打印日志 注意区分引用的是哪个BuildConfig
    public void e(String text) {
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.e(TAG, text);
            }
        }
    }
}
