package com.wxdgut.commonui.test;

import android.app.Application;

import com.wxdgut.commonui.BuildConfig;
import com.wxdgut.commonui.R;
import com.wxdgut.uilibrary.config.UIConfig;
import com.wxdgut.uilibrary.utils.CommonUtils;
import com.wxdgut.uilibrary.utils.UIConfigUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIConfigUtils.init(new UIConfig() {
            @Override
            public int getDefaultDialogLayoutId() {
                return R.layout.dialog_with_et;
            }

            @Override
            public int getDefaultDialogAnimId() {
                return R.style.anim_dialog_upIn_downOut;
            }
        });
        //CommonUtils.globalGray(true);
    }

}
