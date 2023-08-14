package com.wxdgut.commonui.test;

import android.app.Application;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.commonui.R;
import com.wxdgut.uilibrary.iv.UIConfig;
import com.wxdgut.uilibrary.iv.UIConfigUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIConfigUtils.init(new UIConfig() {
            @Override
            public int getDefaultSwitchOnColorId() {
                return BuildConfig.SWITCH_ON_COLOR;
            }

            @Override
            public int getDefaultDialogLayoutId() {
                return R.layout.dialog_with_et;
            }

            @Override
            public int getDefaultDialogAnimId() {
                return R.style.anim_dialog_upIn_downOut;
            }
        });
    }

}
