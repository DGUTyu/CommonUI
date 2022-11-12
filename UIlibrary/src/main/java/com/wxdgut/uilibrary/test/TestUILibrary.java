package com.wxdgut.uilibrary.test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wxdgut.uilibrary.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileName: TestUIlibrary
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 测试UI依赖库是否可用
 */
public class TestUILibrary {
    private static final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * 测试UI依赖库是否可用，轻提示、输出一条Log
     *
     * @param context
     * @param s
     */
    public static void test(Context context, String s) {
        if (s == null || context == null) return;
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        Date date = new Date(System.currentTimeMillis());
        Log.e(BuildConfig.LOG_TAG, dateFormat.format(date) + " TestUILibrary: " + s);
    }
}
