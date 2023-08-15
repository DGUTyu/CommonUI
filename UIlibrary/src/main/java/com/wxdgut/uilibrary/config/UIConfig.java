package com.wxdgut.uilibrary.config;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.dialog.CommonDialog;

/**
 * 抽象类 AppConfig，用于访问 调用者的 BuildConfigField 变量
 */
public abstract class UIConfig {
    public int getDefaultColorId() {
        return R.color.theme_color;
    }

    public int getDefaultImgColorId() {
        return R.color.theme_img;
    }

    public int getDefaultSwitchOnColorId() {
        return R.color.theme_switch_on;
    }

    public int getDefaultSwitchOffColorId() {
        return R.color.theme_switch_off;
    }

    public int getDefaultDialogLayoutId() {
        return R.layout.dialog_fingerprint;
    }

    public int getDefaultDialogAnimId() {
        return CommonDialog.DEFAULT_ANIM;
    }
}
