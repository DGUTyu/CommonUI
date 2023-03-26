package com.wxdgut.commonui.test;

import android.app.Application;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.uilibrary.iv.AppConfig;
import com.wxdgut.uilibrary.iv.ImgProUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImgProUtils.init(new AppConfig() {
            @Override
            public int getDefaultColorId() {
                return BuildConfig.IMG_PRO_COLOR;
            }
        });
    }

}
