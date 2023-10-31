package com.wxdgut.uilibrary.utils;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.dialog.CommonDialog;
import com.wxdgut.uilibrary.config.UIConfig;

/**
 * ImageViewPro的初始化工具类
 */
public class UIConfigUtils {
    private static UIConfig uiConfig;

    public static synchronized boolean init(UIConfig config) {
        if (uiConfig != null) {
            //throw new IllegalStateException("ImageViewPro has already been initialized");
            return false;
        }

        if (!validateAppConfig(config)) {
            //throw new IllegalArgumentException("Invalid AppConfig object");
            return false;
        }

        uiConfig = config;
        return true;
    }

    private static boolean isInitialized() {
        return uiConfig != null;
    }

    public static int getDefaultColorId() {
        if (!isInitialized()) {
            return R.color.theme_color;
        }
        return uiConfig.getDefaultColorId();
    }

    public static int getDefaultImgColorId() {
        if (!isInitialized()) {
            return R.color.theme_img;
        }
        return uiConfig.getDefaultImgColorId();
    }

    public static int getDefaultSwitchOnColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_on;
        }
        return uiConfig.getDefaultSwitchOnColorId();
    }

    public static int getDefaultSwitchOffColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_off;
        }
        return uiConfig.getDefaultSwitchOffColorId();
    }

    public static int getDefaultDialogLayoutId() {
        if (!isInitialized()) {
            return R.layout.dialog_fingerprint;
        }
        return uiConfig.getDefaultDialogLayoutId();
    }

    public static int getDefaultDialogAnimId() {
        if (!isInitialized()) {
            return CommonDialog.DEFAULT_ANIM;
        }
        return uiConfig.getDefaultDialogAnimId();
    }

    public static int getDefaultBtnBgColorId() {
        if (!isInitialized()) {
            return R.color.theme_btn_bg;
        }
        return uiConfig.getDefaultBtnBgColorId();
    }

    public static int getDefaultBtnBgPressColorId() {
        if (!isInitialized()) {
            return R.color.theme_btn_bg_press;
        }
        return uiConfig.getDefaultBtnBgPressColorId();
    }

    public static int getDefaultBtnTvColorId() {
        if (!isInitialized()) {
            return R.color.theme_btn_tv;
        }
        return uiConfig.getDefaultBtnTvColorId();
    }

    public static int getDefaultBtnTvPressColorId() {
        if (!isInitialized()) {
            return R.color.theme_btn_tv_press;
        }
        return uiConfig.getDefaultBtnTvPressColorId();
    }

    public static int getDefaultBtnRadius() {
        if (!isInitialized()) {
            return CommonUtils.dpToPx(6);
        }
        return uiConfig.getDefaultBtnRadius();
    }

    public static int getDefaultBubbleColorId() {
        if (!isInitialized()) {
            return R.color.red;
        }
        return uiConfig.getDefaultBubbleColorId();
    }

    public static int getDefaultBubbleBombDrawableId() {
        if (!isInitialized()) {
            return R.drawable.anim_bubble_pop;
        }
        return uiConfig.getDefaultBubbleBombDrawableId();
    }

    public static int getDefaultBubbleDragRadius() {
        if (!isInitialized()) {
            return 10;
        }
        return uiConfig.getDefaultBubbleDragRadius();
    }

    public static int getDefaultBubbleFixRadiusMin() {
        if (!isInitialized()) {
            return 3;
        }
        return uiConfig.getDefaultBubbleFixRadiusMin();
    }

    private static boolean validateAppConfig(UIConfig uiConfig) {
        if (uiConfig == null) {
            return false;
        }

        try {
            // 尝试调用方法以验证是否存在
            uiConfig.getDefaultColorId();
            uiConfig.getDefaultImgColorId();
            uiConfig.getDefaultSwitchOnColorId();
            uiConfig.getDefaultSwitchOffColorId();
            uiConfig.getDefaultDialogLayoutId();
            uiConfig.getDefaultDialogAnimId();
            uiConfig.getDefaultBtnBgColorId();
            uiConfig.getDefaultBtnBgPressColorId();
            uiConfig.getDefaultBtnTvColorId();
            uiConfig.getDefaultBtnTvPressColorId();
            uiConfig.getDefaultBtnRadius();
            uiConfig.getDefaultBubbleColorId();
            uiConfig.getDefaultBubbleBombDrawableId();
            uiConfig.getDefaultBubbleDragRadius();
            uiConfig.getDefaultBubbleFixRadiusMin();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}