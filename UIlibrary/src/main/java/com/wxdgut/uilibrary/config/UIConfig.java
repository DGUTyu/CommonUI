package com.wxdgut.uilibrary.config;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.dialog.CommonDialog;
import com.wxdgut.uilibrary.utils.DensityUtil;

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

    public int getDefaultBtnBgColorId() {
        return R.color.theme_btn_bg;
    }

    public int getDefaultBtnBgPressColorId() {
        return R.color.theme_btn_bg_press;
    }

    public int getDefaultBtnTvColorId() {
        return R.color.theme_btn_tv;
    }

    public int getDefaultBtnTvPressColorId() {
        return R.color.theme_btn_tv_press;
    }

    public int getDefaultBtnRadius() {
        return DensityUtil.dpToPx(6);
    }
}
