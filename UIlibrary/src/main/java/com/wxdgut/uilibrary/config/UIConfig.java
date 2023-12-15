package com.wxdgut.uilibrary.config;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.dialog.CommonDialog;
import com.wxdgut.uilibrary.utils.CommonUtils;

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
        return CommonUtils.dpToPx(6);
    }

    public int getDefaultBubbleColorId() {
        return R.color.red;
    }

    public int getDefaultBubbleBombDrawableId() {
        return R.drawable.anim_bubble_pop;
    }

    public int getDefaultBubbleDragRadius() {
        return 10;
    }

    public int getDefaultBubbleFixRadiusMin() {
        return 3;
    }

    public int[] getDefaultLoveImgIds() {
        return new int[]{R.drawable.img_like_red, R.drawable.img_like_cyan,
                R.drawable.img_like_gold, R.drawable.img_like_green,
                R.drawable.img_like_blue, R.drawable.img_like_lime,
                R.drawable.img_like_magenta, R.drawable.img_like_orange,
                R.drawable.img_like_orangered, R.drawable.img_like_pinkpurple,
                R.drawable.img_like_purple, R.drawable.img_like_arcoblue,
                R.drawable.img_like_yellow};
    }

    public int getDefaultLovePerClick() {
        return 1;
    }
}
