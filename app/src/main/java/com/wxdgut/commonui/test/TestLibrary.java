package com.wxdgut.commonui.test;

import android.util.Log;

import com.wxdgut.commonui.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileName: TestLibrary
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 测试依赖库是否可用
 */
public class TestLibrary {
    private static final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * 测试依赖库是否可用，输出一条Log
     *
     * @param s
     */
    public static void test(String s) {
        if (s == null) return;
        Date date = new Date(System.currentTimeMillis());
        Log.e(BuildConfig.LOG_TAG, dateFormat.format(date) + " TestLibrary: " + s);
    }
}
