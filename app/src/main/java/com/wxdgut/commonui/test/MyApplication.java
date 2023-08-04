package com.wxdgut.commonui.test;

import android.app.Application;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.uilibrary.iv.AppConfig;
import com.wxdgut.uilibrary.iv.ColorConfigUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ColorConfigUtils.init(new AppConfig() {
            @Override
            public int getDefaultSwitchOnColorId() {
                return BuildConfig.SWITCH_ON_COLOR;
            }
        });
    }

}
